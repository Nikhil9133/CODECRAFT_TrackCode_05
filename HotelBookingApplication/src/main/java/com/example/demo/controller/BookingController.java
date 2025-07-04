package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Booking;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingService;

@RestController
public class BookingController{
    
	@Autowired
    private BookingService bookingService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    // Create a new booking
    @PostMapping("/newBooking")
    public ResponseEntity<?> createBooking(@RequestParam Long roomId,
                                           @RequestParam String checkIn,
                                           @RequestParam String checkOut,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (roomOpt.isEmpty()) {
        	logger.info("Room with the given id "+roomId+" is not available");
            return ResponseEntity.badRequest().body("Room not found");
        }

        Optional<User> userOpt = userRepository.findByUsername(userDetails.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);

        if (checkOutDate.isBefore(checkInDate)) {
            return ResponseEntity.badRequest().body("Check-out date must be after check-in date");
        }

        LocalDate availableFrom = roomOpt.get().getAvailableFrom();
        LocalDate availableTo = roomOpt.get().getAvailableTo();

        if (checkInDate.isBefore(availableFrom) || checkOutDate.isAfter(availableTo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Room is only available between " + availableFrom + " and " + availableTo);
        }
        List<Booking> AlreadyBooked = bookingRepository.findOverlappingBookings(
        	    roomId, checkInDate, checkOutDate
        	);

        	if (!AlreadyBooked.isEmpty()) {
        	    logger.warn("Room {} is already booked between {} and {}", roomId, checkIn, checkOut);
        	    return ResponseEntity.status(HttpStatus.CONFLICT)
        	        .body("Room is already booked for the selected dates. Please choose a different date range.");
        	}

        Booking booking = new Booking();
        booking.setRoom(roomOpt.get());
        booking.setUser(userOpt.get());
        booking.setCheckIn(checkInDate);
        booking.setCheckOut(checkOutDate);

        Booking savedBooking = bookingService.book(booking);
        logger.info("Room booked successfully");
        return ResponseEntity.ok(savedBooking);
    }

    // Get all bookings for the authenticated user
    @GetMapping("/myBookings")
    public ResponseEntity<?> getMyBookings(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> userOpt = userRepository.findByUsername(userDetails.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        List<Booking> bookings = bookingService.getBookingsByUser(userOpt.get());
        logger.info("My Bookings are "+ bookings);
        return ResponseEntity.ok(bookings);
    }

    // Cancel a booking
    @DeleteMapping("/cancelBooking/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Booking> bookingOpt = bookingService.getBookingById(id);
        if (bookingOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Booking not found");
        }

        Booking booking = bookingOpt.get();
        if (!booking.getUser().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).body("You can only cancel your own bookings");
        }

        bookingService.cancelBooking(id);
        logger.info("Booking with the given id "+id+" is cancelled successfully");
        return ResponseEntity.ok("Booking cancelled successfully");
    }
}

