package com.example.demo.repository;

import com.example.demo.entity.Workout;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, String> {
    List<Workout> findByUserOrderByCreatedAtDesc(User user);

    Optional<Workout> findByIdAndUser(String id, User user);

    List<Workout> findByUserAndDateOrderByCreatedAtDesc(User user, String date);
}
