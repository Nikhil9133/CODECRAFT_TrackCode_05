package com.project.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Booking_Id")
	private Long id;

	@Column(name ="Check_In_Date")
	private LocalDate checkIn;
	
	@Column(name ="Check_Out_Date")
	private LocalDate checkOut;

	@ManyToOne
	private User user;

	@ManyToOne
	private Room room;

}
