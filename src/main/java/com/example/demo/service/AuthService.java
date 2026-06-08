package com.example.demo.service;

import com.example.demo.dto.LoginRegisterDTO;
import com.example.demo.dto.AuthResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponseDTO register(LoginRegisterDTO loginRegisterDTO) {
        if (userRepository.existsByEmail(loginRegisterDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email já está registrado");
        }

        User user = new User();
        user.setEmail(loginRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(loginRegisterDTO.getPassword()));
        user.setDisplayName(loginRegisterDTO.getDisplayName());

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(savedUser);

        return new AuthResponseDTO(token, savedUser.getEmail(), savedUser.getId(), savedUser.getDisplayName());
    }

    public AuthResponseDTO login(LoginRegisterDTO loginRegisterDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRegisterDTO.getEmail(),
                            loginRegisterDTO.getPassword()));

            User user = (User) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(user);

            return new AuthResponseDTO(token, user.getEmail(), user.getId(), user.getDisplayName());
        } catch (AuthenticationException e) {
            throw new ResourceNotFoundException("Email ou senha inválidos");
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }
}
