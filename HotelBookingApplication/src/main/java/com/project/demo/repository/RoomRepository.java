package com.project.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.demo.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	List<Room> findByAvailableFromLessThanEqualAndAvailableToGreaterThanEqual(LocalDate checkIn, LocalDate checkOut);

}
