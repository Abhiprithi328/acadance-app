package com.acadance.app.service;

import com.acadance.app.dto.AuthResponse;
import com.acadance.app.dto.LoginRequest;
import com.acadance.app.dto.SignupRequest;
import com.acadance.app.entity.User;
import com.acadance.app.repository.UserRepository;
import com.acadance.app.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("That name is already taken. Try another.");
        }

        User user = new User(request.getName(), passwordEncoder.encode(request.getPassword()));
        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId(), user.getName());
        return new AuthResponse(token, user.getId(), user.getName(), user.getAttendanceRequirement());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid name or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid name or password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getName());
        return new AuthResponse(token, user.getId(), user.getName(), user.getAttendanceRequirement());
    }
}
