package com.example.Reservation_System.model;

import java.util.List;

public class Route {

    // Predefined static route for Pune–Shirdi Express
    public static final List<String> STATIONS = List.of("Pune", "Daund", "Ahmednagar", "Belapur","Shirdi");

    // Instance field for any custom route
    private List<String> stations;

    // Default constructor
    public Route() {}

    // Constructor to create a custom route
    public Route(List<String> stations) {
        this.stations = stations;
    }

    // Getter
    public List<String> getStations() {
        return stations;
    }

    // Setter
    public void setStations(List<String> stations) {
        this.stations = stations;
    }
}
