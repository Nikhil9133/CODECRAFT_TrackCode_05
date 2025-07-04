package com.project.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.demo.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	@Query("SELECT b FROM Booking b WHERE b.room.id = :roomId " +
		       "AND ((b.checkIn <= :checkOut AND b.checkOut >= :checkIn))")
		List<Booking> findOverlappingBookings(Long roomId, LocalDate checkIn, LocalDate checkOut);

}
