package com.project.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.demo.model.Room;
import com.project.demo.service.RoomService;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired 
    private RoomService roomService;
    
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @PostMapping
    public Room create(@RequestBody Room room) {
    	logger.info("Room Created Successfully");
        return roomService.create(room);
    }

    @GetMapping("/search")
    public List<Room> search(@RequestParam LocalDate checkIn, @RequestParam LocalDate checkOut) {
        return roomService.search(checkIn, checkOut);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
    	logger.info("Room with the id " + id +" deleted Successfully");
        roomService.delete(id);
    }
}

