package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutResponseDTO {
    private String id;
    private String userId;
    private String date;
    private String gymName;
    private String rawText;
    private Long createdAt;
    private Long updatedAt;
    private List<ExerciseDTO> exercises;
}
