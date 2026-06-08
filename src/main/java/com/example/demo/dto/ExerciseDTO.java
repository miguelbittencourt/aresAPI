package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDTO {
    private String id;
    private Integer orderIndex;
    private String name;
    private String notes;
    private List<SetDTO> sets;
}
