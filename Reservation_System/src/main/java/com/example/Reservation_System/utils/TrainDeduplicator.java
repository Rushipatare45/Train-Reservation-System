package com.example.Reservation_System.utils;

import com.example.Reservation_System.model.Train;
import com.example.Reservation_System.repository.TrainRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Component
public class TrainDeduplicator {

    private final TrainRepository trainRepository;

    public TrainDeduplicator(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void removeDuplicateTrains() {
        List<Train> trains = trainRepository.findAll();
        Set<String> seen = new HashSet<>();
        
        for (Train train : trains) {
            // Create a unique key based on Name + DepartureTime + Stops
            String key = train.getName() + "|" + train.getDepartureTime() + "|" + train.getStops().toString();
            if (seen.contains(key)) {
                // Duplicate found → delete it
                trainRepository.delete(train);
            } else {
                seen.add(key);
            }
        }

        System.out.println("✅ Duplicate trains removed successfully!");
    }
}
