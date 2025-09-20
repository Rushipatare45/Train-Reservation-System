package com.example.Reservation_System.service;

import com.example.Reservation_System.model.Train;
import com.example.Reservation_System.repository.TrainRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainService {

    private final TrainRepository trainRepository;

    // Constructor-based injection
    public TrainService(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    // Get all trains
    public List<Train> getAllTrains() {
        return trainRepository.findAll(); // ✅ built-in JPA method
    }

    // Get train by ID
    public Train getTrainById(Long id) {
        return trainRepository.findById(id)
                              .orElse(null); // ✅ built-in JPA method
    }
}
