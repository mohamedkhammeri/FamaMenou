package com.project.famaMenouApp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserDTO user;

    // Constructor for your specific case
    public AuthResponseDTO(String accessToken, UserDTO user) {
        this.accessToken = accessToken;
        this.user = user;
        this.tokenType = "Bearer"; // Set default value
    }
}