package com.example.Reservation_System.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.*;

@Entity
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int totalSeats = 40;

    @ManyToOne
    @JoinColumn(name = "train_id")
    @JsonBackReference  // prevents infinite recursion with Train
    private Train train;

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Seat> seats = new ArrayList<>();

    @Transient
    private Map<String, List<Segment>> seatSegments = new HashMap<>();

    public Coach() {}

    public Coach(String name, int totalSeats) {
        this.name = name;
        this.totalSeats = totalSeats;
        initializeSeatsIfNeeded();
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public Train getTrain() {
        return train;
    }

    public void initializeSeatsIfNeeded() {
        if (seatSegments == null) {
            seatSegments = new HashMap<>();
        }
        for (int i = 1; i <= totalSeats; i++) {
            seatSegments.putIfAbsent("S" + i, new ArrayList<>());
        }
    }

    public Map<String, List<Segment>> getSeatSegments() { return seatSegments; }
    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public static class Segment {
        private String fromStation;
        private String toStation;

        public Segment() {}
        public Segment(String fromStation, String toStation) {
            this.fromStation = fromStation;
            this.toStation = toStation;
        }

        public String getFromStation() { return fromStation; }
        public void setFromStation(String fromStation) { this.fromStation = fromStation; }
        public String getToStation() { return toStation; }
        public void setToStation(String toStation) { this.toStation = toStation; }
    }
}
