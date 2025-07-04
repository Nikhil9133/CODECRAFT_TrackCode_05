package com.project.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.demo.model.Room;
import com.project.demo.repository.RoomRepository;

@Service
public class RoomService {
    @Autowired 
    private RoomRepository roomRepository;

    public Room create(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> search(LocalDate checkIn, LocalDate checkOut) {
        return roomRepository.findByAvailableFromLessThanEqualAndAvailableToGreaterThanEqual(checkIn, checkOut);
    }

    public void delete(Long id) {
        roomRepository.deleteById(id);
    }
}
