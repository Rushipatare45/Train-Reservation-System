package com.example.Reservation_System.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String departureTime;

    @ElementCollection
    private List<String> stops = new ArrayList<>();

    private int price;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference  // Prevent circular reference with Coach
    private List<Coach> coaches = new ArrayList<>();

    public Train() {}

    public Train(String name, String departureTime, List<String> stops, List<Coach> coaches, int price) {
        this.name = name;
        this.departureTime = departureTime;
        this.stops = (stops != null) ? stops : new ArrayList<>();
        this.price = price;

        if (coaches != null) {
            for (Coach coach : coaches) {
                addCoach(coach);
            }
        }
    }

    public void addCoach(Coach coach) {
        if (coach != null) {
            coach.setTrain(this);
            if (!this.coaches.contains(coach)) {
                this.coaches.add(coach);
            }
        }
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public List<String> getStops() { return stops; }
    public void setStops(List<String> stops) { this.stops = stops; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public List<Coach> getCoaches() { return coaches; }
    public void setCoaches(List<Coach> coaches) { this.coaches = coaches; }
}
