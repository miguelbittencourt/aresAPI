package com.example.demo.controller;

import com.example.demo.dto.CreateWorkoutDTO;
import com.example.demo.dto.WorkoutResponseDTO;
import com.example.demo.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Workouts", description = "APIs para gerenciamento de treinos")
@SecurityRequirement(name = "bearerAuth")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private AuthService authService;

    private Long getUserIdFromAuth(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return authService.getUserByEmail(userDetails.getUsername()).getId();
    }

    @PostMapping
    @Operation(summary = "Criar novo treino", description = "Cria um novo treino para o usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Treino criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<WorkoutResponseDTO> createWorkout(
            Authentication authentication,
            @Valid @RequestBody CreateWorkoutDTO createWorkoutDTO) {
        Long userId = getUserIdFromAuth(authentication);
        WorkoutResponseDTO response = workoutService.createWorkout(userId, createWorkoutDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar treinos", description = "Retorna todos os treinos do usuário autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de treinos retornada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<List<WorkoutResponseDTO>> getWorkouts(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        List<WorkoutResponseDTO> workouts = workoutService.getWorkoutsByUser(userId);
        return ResponseEntity.ok(workouts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponseDTO> getWorkout(
            Authentication authentication,
            @PathVariable String id) {
        Long userId = getUserIdFromAuth(authentication);
        WorkoutResponseDTO workout = workoutService.getWorkout(userId, id);
        return ResponseEntity.ok(workout);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<WorkoutResponseDTO>> getWorkoutsByDate(
            Authentication authentication,
            @PathVariable String date) {
        Long userId = getUserIdFromAuth(authentication);
        List<WorkoutResponseDTO> workouts = workoutService.getWorkoutsByUserAndDate(userId, date);
        return ResponseEntity.ok(workouts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutResponseDTO> updateWorkout(
            Authentication authentication,
            @PathVariable String id,
            @Valid @RequestBody CreateWorkoutDTO createWorkoutDTO) {
        Long userId = getUserIdFromAuth(authentication);
        WorkoutResponseDTO workout = workoutService.updateWorkout(userId, id, createWorkoutDTO);
        return ResponseEntity.ok(workout);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(
            Authentication authentication,
            @PathVariable String id) {
        Long userId = getUserIdFromAuth(authentication);
        workoutService.deleteWorkout(userId, id);
        return ResponseEntity.noContent().build();
    }
}
