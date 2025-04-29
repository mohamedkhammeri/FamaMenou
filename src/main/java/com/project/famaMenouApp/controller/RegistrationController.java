package com.project.famaMenouApp.controller;

import com.project.famaMenouApp.model.dto.RegisterVM;
import com.project.famaMenouApp.model.entity.User;
import com.project.famaMenouApp.repository.UserRepository;
import com.project.famaMenouApp.service.impl.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class RegistrationController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserService userService

    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService= userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterVM registerVM) {
        // Check if login (username) already exists
        if (userRepository.findOneByLogin(registerVM.getUsername()).isPresent()) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }

        // Check if email already exists
        if (userRepository.findOneByEmailIgnoreCase(registerVM.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        // Create new user
        User user = new User();
        user.setLogin(registerVM.getUsername());
        user.setPassword(passwordEncoder.encode(registerVM.getPassword()));
        user.setEmail(registerVM.getEmail());
        user.setAuthorities(registerVM.getAuthorities());

        // Save user to repository
        userService.registerUser(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
}