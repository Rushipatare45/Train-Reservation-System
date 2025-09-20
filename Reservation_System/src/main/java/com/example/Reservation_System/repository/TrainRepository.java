package com.example.Reservation_System.repository;

import com.example.Reservation_System.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    // No need to add methods manually unless you want custom queries
    // findAll() and findById() are already provided by JpaRepository
     Optional<Train> findByName(String name);
}
