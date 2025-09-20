package com.example.Reservation_System.repository;

import com.example.Reservation_System.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings for a specific train
    List<Booking> findByTrainId(Long trainId);

    // Find booking by seat number and train to check availability
    Booking findByTrainIdAndSeatNumber(Long trainId, int seatNumber);

    // Find bookings for a specific train and date
    List<Booking> findByTrainIdAndDate(Long trainId, String date);

    // ✅ New: Find bookings by user
    List<Booking> findByUserId(Long userId); // returns only bookings of the specific user
}
