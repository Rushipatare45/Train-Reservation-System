package com.example.Reservation_System.service;

import com.example.Reservation_System.model.Booking;
import com.example.Reservation_System.model.Coach;
import com.example.Reservation_System.model.Train;
import com.example.Reservation_System.repository.BookingRepository;
import com.example.Reservation_System.repository.TrainRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TrainBookingInitializer {

    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;

    public TrainBookingInitializer(TrainRepository trainRepository,
                                   BookingRepository bookingRepository) {
        this.trainRepository = trainRepository;
        this.bookingRepository = bookingRepository;
    }

    @PostConstruct
    public void init() {
        List<Booking> bookings = bookingRepository.findAll();

        for (Booking booking : bookings) {
            // Fetch train safely
            Train train = trainRepository.findById(booking.getTrain().getId())
                                         .orElse(null);
            if (train == null) continue;

            // Find the correct coach
            train.getCoaches().stream()
                .filter(coach -> coach.getName().equals(booking.getCoachName()))
                .findFirst()
                .ifPresent(coach -> {
                    // ✅ Ensure seatSegments map is initialized
                    coach.initializeSeatsIfNeeded();

                    // Convert seatNumber to key string ("S1", "S2", etc.)
                    String seatKey = "S" + booking.getSeatNumber();

                    Map<String, List<Coach.Segment>> seatSegments = coach.getSeatSegments();

                    // Ensure the seat entry exists
                    seatSegments.putIfAbsent(seatKey, new ArrayList<>());

                    // Add booked segment
                    seatSegments.get(seatKey)
                                .add(new Coach.Segment(booking.getFromStation(), booking.getToStation()));
                });
        }

        System.out.println("✅ Booked seat segments initialized from database.");
    }
}
