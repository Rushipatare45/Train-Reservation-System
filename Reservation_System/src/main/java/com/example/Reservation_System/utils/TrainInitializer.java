package com.example.Reservation_System.utils;

import com.example.Reservation_System.model.Coach;
import com.example.Reservation_System.model.Seat;
import com.example.Reservation_System.model.Train;
import com.example.Reservation_System.repository.TrainRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class TrainInitializer {

    private final TrainRepository trainRepository;

    public TrainInitializer(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @PostConstruct
    public void init() {
        // List of trains to add
       List<Train> trainsToAdd = Arrays.asList(
    // Shirdi trains
    createTrain("Pune–Shirdi Fast Passenger", "04:20", Arrays.asList(
        "Pune", "Daund", "Belapur", "Shirdi"), 100, 2, 10),
    createTrain("Mumbai Dadar–Shirdi Express", "03:35", Arrays.asList(
        "Mumbai Dadar", "Thane", "Kalyan", "Igatpuri", "Manmad", "Shirdi"), 120, 3, 10),
    createTrain("Mumbai CSMT–Shirdi Vande Bharat", "06:20", Arrays.asList(
        "Mumbai CSMT", "Thane", "Kalyan", "Nashik Road", "Sainagar Shirdi"), 150, 3, 10),

    // Other routes
    createTrain("Pune–Mumbai Express", "05:30", Arrays.asList(
        "Pune", "Lonavala", "Khandala", "Karjat", "Thane", "Mumbai CSMT"), 200, 3, 12),
    createTrain("Pune–Nagpur Superfast", "08:00", Arrays.asList(
        "Pune", "Manmad", "Nagpur", "Wardha", "Balharshah"), 800, 3, 12),
    createTrain("Mumbai–Delhi Rajdhani", "16:30", Arrays.asList(
        "Mumbai CSMT", "Vasai Road", "Igatpuri", "Bhopal", "Agra Cantt", "New Delhi"), 1400, 3, 15),
    createTrain("Pune–Goa Express", "07:15", Arrays.asList(
        "Pune", "Kolhapur", "Madgaon", "Vasco da Gama"), 450, 2, 10),
    createTrain("Mumbai–Ahmedabad Express", "06:50", Arrays.asList(
        "Mumbai CSMT", "Vapi", "Surat", "Vadodara", "Ahmedabad"), 500, 2, 11),
    createTrain("Pune–Hyderabad Express", "09:00", Arrays.asList(
        "Pune", "Solapur", "Nizamabad", "Hyderabad"), 600, 2, 12),
    createTrain("Mumbai–Bangalore Express", "14:00", Arrays.asList(
        "Mumbai CSMT", "Pune", "Belgaum", "Hubli", "Bangalore"), 980, 3, 12),
    createTrain("Pune–Kolkata Express", "10:30", Arrays.asList(
        "Pune", "Nagpur", "Raipur", "Kolkata"), 1600, 3, 14),
    createTrain("Mumbai–Chennai Express", "15:00", Arrays.asList(
        "Mumbai CSMT", "Pune", "Solapur", "Vijayawada", "Chennai"), 1400, 3, 13),
    createTrain("Pune–Jaipur Express", "12:00", Arrays.asList(
        "Pune", "Manmad", "Bhopal", "Jaipur"), 1200, 3, 12),
    createTrain("Mumbai–Lucknow Express", "16:45", Arrays.asList(
        "Mumbai CSMT", "Nashik", "Bhopal", "Kanpur", "Lucknow"), 1400, 3, 12),
    createTrain("Pune–Bhubaneswar Express", "07:30", Arrays.asList(
        "Pune", "Nagpur", "Raipur", "Bhubaneswar"), 1500, 3, 12)
);


        for (Train train : trainsToAdd) {
            Optional<Train> existing = trainRepository.findByName(train.getName());
            if (existing.isEmpty()) {  // ✅ only save if train doesn't exist
                trainRepository.save(train);
                System.out.println("✅ Added train: " + train.getName());
            } else {
                System.out.println("ℹ️ Train already exists: " + train.getName());
            }
        }
    }

    // Utility method to create a train with linked coaches and seats
    private Train createTrain(String name, String departureTime, List<String> stops, int price, int coachCount, int seatsPerCoach) {
        Train train = new Train();
        train.setName(name);
        train.setDepartureTime(departureTime);
        train.setStops(stops);
        train.setPrice(price);

        // Create coaches and seats
        for (int i = 1; i <= coachCount; i++) {
            Coach coach = new Coach();
            coach.setName("S" + i);
            coach.setTrain(train);

            for (int s = 1; s <= seatsPerCoach; s++) {
                Seat seat = new Seat("S" + s, coach, false);
                coach.getSeats().add(seat);
            }

            train.addCoach(coach);
        }

        return train;
    }
}
