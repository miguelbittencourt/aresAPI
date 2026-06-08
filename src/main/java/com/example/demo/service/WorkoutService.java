package com.example.demo.service;

import com.example.demo.dto.CreateWorkoutDTO;
import com.example.demo.dto.ExerciseDTO;
import com.example.demo.dto.SetDTO;
import com.example.demo.dto.WorkoutResponseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.entity.ExerciseSet;
import com.example.demo.entity.User;
import com.example.demo.entity.Workout;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.repository.ExerciseSetRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseSetRepository exerciseSetRepository;

    @Transactional
    public WorkoutResponseDTO createWorkout(Long userId, CreateWorkoutDTO createWorkoutDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Workout workout = new Workout();
        workout.setUser(user);
        workout.setDate(createWorkoutDTO.getDate());
        workout.setGymName(createWorkoutDTO.getGymName());
        workout.setRawText(createWorkoutDTO.getRawText());

        Workout savedWorkout = workoutRepository.save(workout);

        // Criar exercícios
        List<Exercise> exercises = createWorkoutDTO.getExercises().stream()
                .map(exerciseDTO -> createExercise(savedWorkout, exerciseDTO))
                .collect(Collectors.toList());

        savedWorkout.setExercises(exercises);
        Workout finalWorkout = workoutRepository.save(savedWorkout);

        return mapToWorkoutResponseDTO(finalWorkout);
    }

    private Exercise createExercise(Workout workout, ExerciseDTO exerciseDTO) {
        Exercise exercise = new Exercise();
        exercise.setWorkout(workout);
        exercise.setOrderIndex(exerciseDTO.getOrderIndex());
        exercise.setName(exerciseDTO.getName());
        exercise.setNotes(exerciseDTO.getNotes());

        Exercise savedExercise = exerciseRepository.save(exercise);

        // Criar sets
        List<ExerciseSet> sets = exerciseDTO.getSets().stream()
                .map(setDTO -> createExerciseSet(savedExercise, setDTO))
                .collect(Collectors.toList());

        savedExercise.setSets(sets);
        return exerciseRepository.save(savedExercise);
    }

    private ExerciseSet createExerciseSet(Exercise exercise, SetDTO setDTO) {
        ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setExercise(exercise);
        exerciseSet.setReps(setDTO.getReps());
        exerciseSet.setWeight(setDTO.getWeight());
        exerciseSet.setUnit(setDTO.getUnit());
        return exerciseSetRepository.save(exerciseSet);
    }

    public WorkoutResponseDTO getWorkout(Long userId, String workoutId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Workout workout = workoutRepository.findByIdAndUser(workoutId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Treino não encontrado"));

        return mapToWorkoutResponseDTO(workout);
    }

    public List<WorkoutResponseDTO> getWorkoutsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return workoutRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapToWorkoutResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WorkoutResponseDTO> getWorkoutsByUserAndDate(Long userId, String date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return workoutRepository.findByUserAndDateOrderByCreatedAtDesc(user, date).stream()
                .map(this::mapToWorkoutResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkoutResponseDTO updateWorkout(Long userId, String workoutId, CreateWorkoutDTO createWorkoutDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Workout workout = workoutRepository.findByIdAndUser(workoutId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Treino não encontrado"));

        workout.setDate(createWorkoutDTO.getDate());
        workout.setGymName(createWorkoutDTO.getGymName());
        workout.setRawText(createWorkoutDTO.getRawText());

        // Remover exercícios antigos
        workout.getExercises().clear();

        // Adicionar novos exercícios
        List<Exercise> newExercises = createWorkoutDTO.getExercises().stream()
                .map(exerciseDTO -> createExercise(workout, exerciseDTO))
                .collect(Collectors.toList());

        workout.setExercises(newExercises);
        Workout updatedWorkout = workoutRepository.save(workout);

        return mapToWorkoutResponseDTO(updatedWorkout);
    }

    @Transactional
    public void deleteWorkout(Long userId, String workoutId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Workout workout = workoutRepository.findByIdAndUser(workoutId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Treino não encontrado"));

        workoutRepository.delete(workout);
    }

    private WorkoutResponseDTO mapToWorkoutResponseDTO(Workout workout) {
        WorkoutResponseDTO dto = new WorkoutResponseDTO();
        dto.setId(workout.getId());
        dto.setUserId(workout.getUser().getId().toString());
        dto.setDate(workout.getDate());
        dto.setGymName(workout.getGymName());
        dto.setRawText(workout.getRawText());
        dto.setCreatedAt(workout.getCreatedAt());
        dto.setUpdatedAt(workout.getUpdatedAt());

        dto.setExercises(workout.getExercises().stream()
                .map(this::mapToExerciseDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private ExerciseDTO mapToExerciseDTO(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(exercise.getId());
        dto.setOrderIndex(exercise.getOrderIndex());
        dto.setName(exercise.getName());
        dto.setNotes(exercise.getNotes());

        dto.setSets(exercise.getSets().stream()
                .map(this::mapToSetDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private SetDTO mapToSetDTO(ExerciseSet exerciseSet) {
        SetDTO dto = new SetDTO();
        dto.setReps(exerciseSet.getReps());
        dto.setWeight(exerciseSet.getWeight());
        dto.setUnit(exerciseSet.getUnit());
        return dto;
    }
}
