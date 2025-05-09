package com.project.famaMenouApp.controller;

import com.project.famaMenouApp.model.dto.RegisterVM;
import com.project.famaMenouApp.model.dto.UserDTO;
import com.project.famaMenouApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterVM registerVM) {
        UserDTO userDTO = convertToUserDTO(registerVM);
        UserDTO registeredUser = userService.registerUser(userDTO);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    private UserDTO convertToUserDTO(RegisterVM registerVM) {
        return UserDTO.builder()
                .login(registerVM.getUsername().toLowerCase())
                .email(registerVM.getEmail())
                .firstName(registerVM.getFirstName())
                .lastName(registerVM.getLastName())
                .activated(false) // New users are inactive by default
                .termsOfServiceAccepted(registerVM.isAcceptTerms())
                .promotionAccepted(registerVM.isAcceptPromotions())
                .build();
    }
}