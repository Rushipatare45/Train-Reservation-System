package com.example.Reservation_System.utils;

import java.util.List;

public class FareCalculator {

    private static final int BASE_FARE = 45; // ₹45 per station

    public static int calculateFare(List<String> stops, String from, String to) {
        int fromIndex = stops.indexOf(from);
        int toIndex = stops.indexOf(to);

        if (fromIndex == -1 || toIndex == -1 || fromIndex >= toIndex) {
            throw new IllegalArgumentException("Invalid route selection");
        }

        // Difference in stops × base fare
        int distance = toIndex - fromIndex;
        return distance * BASE_FARE;
    }
}
