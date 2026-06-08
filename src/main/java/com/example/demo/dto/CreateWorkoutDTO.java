package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkoutDTO {
    @NotBlank(message = "Nome da academia é obrigatório")
    private String gymName;

    @NotBlank(message = "Data é obrigatória")
    private String date;

    private String rawText;

    @NotEmpty(message = "Deve conter pelo menos um exercício")
    private List<ExerciseDTO> exercises;
}
