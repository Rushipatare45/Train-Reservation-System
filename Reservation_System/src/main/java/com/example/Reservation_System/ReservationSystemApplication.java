package com.example.Reservation_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReservationSystemApplication {
	public static void main(String[] args)
	{
		// Starting point of the Reservation System application 
		// This method launches the Spring Boot application
		System.out.println("Starting Reservation System Application...");
		SpringApplication.run(ReservationSystemApplication.class, args);
	}

}
