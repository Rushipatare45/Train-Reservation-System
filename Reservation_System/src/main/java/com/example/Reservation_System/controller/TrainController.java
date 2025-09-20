package com.example.Reservation_System.controller;

import com.example.Reservation_System.model.Train;
import com.example.Reservation_System.repository.TrainRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trains")
@CrossOrigin(origins = "http://localhost:3000")
public class TrainController {

    private final TrainRepository trainRepository;

    public TrainController(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    // Search trains by station order (case-insensitive)
    @GetMapping("/search")
    public List<Train> searchTrains(@RequestParam String fromStation,
                                    @RequestParam String toStation) {
        String from = fromStation.trim().toLowerCase();
        String to = toStation.trim().toLowerCase();

        return trainRepository.findAll().stream()
                .filter(train -> {
                    List<String> lowerStops = train.getStops().stream()
                            .map(s -> s.toLowerCase())
                            .collect(Collectors.toList());
                    return lowerStops.contains(from) &&
                           lowerStops.contains(to) &&
                           lowerStops.indexOf(from) < lowerStops.indexOf(to);
                })
                .collect(Collectors.toList());
    }

    // Get all trains
    @GetMapping("/all")
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    // Get train by ID
    @GetMapping("/{id}")
    public ResponseEntity<Train> getTrainById(@PathVariable Long id) {
        return trainRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
