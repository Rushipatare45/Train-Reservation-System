package com.example.Reservation_System.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;

    private boolean booked;

    @ManyToOne
    @JoinColumn(name = "coach_id")
    @JsonBackReference  // prevents infinite recursion with Coach
    private Coach coach;

    public Seat() {}

    public Seat(String seatNumber, Coach coach, boolean booked) {
        this.seatNumber = seatNumber;
        this.coach = coach;
        this.booked = booked;
    }

    public Long getId() { return id; }
    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }
    public Coach getCoach() { return coach; }
    public void setCoach(Coach coach) { this.coach = coach; }
}
