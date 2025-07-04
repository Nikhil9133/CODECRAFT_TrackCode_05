package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Booking;
import com.example.demo.model.User;
import com.example.demo.repository.BookingRepository;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking book(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public void cancelBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}

