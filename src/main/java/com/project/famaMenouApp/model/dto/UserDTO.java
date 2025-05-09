package com.project.famaMenouApp.model.dto;

import com.project.famaMenouApp.model.entity.Authority;
import com.project.famaMenouApp.model.entity.User;
import lombok.*;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String login;
    private String password; // Added password field
    private String firstName;
    private String lastName;
    private String email;
    private boolean activated;
    private String phoneNumber;
    private boolean phoneActivated;
    private String langKey;
    private String imageUrl;
    private String position;
    private Double rangeUser;
    private Set<String> authorities;
    private String tokenDevice;
    private boolean termsOfServiceAccepted;
    private boolean promotionAccepted;
    private boolean emailNotifications;
    private Instant resetDate;
    private String stripeAccountId;
    private String stripeCustomerId;

    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(null) // Never expose password hash in DTO
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .activated(user.isActivated())
                .phoneNumber(user.getPhoneNumber())
                .phoneActivated(user.isPhoneActivated())
                .langKey(user.getLangKey())
                .imageUrl(user.getImageUrl())
                .position(user.getPosition())
                .rangeUser(user.getRangeUser())
                .authorities(user.getAuthorities().stream()
                        .map(Authority::getName)
                        .collect(Collectors.toSet()))
                .tokenDevice(user.getTokenDevice())
                .termsOfServiceAccepted(user.isTermsOfServiceAccepted())
                .promotionAccepted(user.isPromotionAccepted())
                .emailNotifications(user.isEmailNotifications())
                .resetDate(user.getResetDate())
                .stripeAccountId(user.getStripeAccountId())
                .stripeCustomerId(user.getStripeCustomerId())
                .build();
    }

    public static User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setLogin(userDTO.getLogin());
        // Password is handled separately in service layer
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setActivated(userDTO.isActivated());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPhoneActivated(userDTO.isPhoneActivated());
        user.setLangKey(userDTO.getLangKey());
        user.setImageUrl(userDTO.getImageUrl());
        user.setPosition(userDTO.getPosition());
        user.setRangeUser(userDTO.getRangeUser());
        user.setTokenDevice(userDTO.getTokenDevice());
        user.setTermsOfServiceAccepted(userDTO.isTermsOfServiceAccepted());
        user.setPromotionAccepted(userDTO.isPromotionAccepted());
        user.setEmailNotifications(userDTO.isEmailNotifications());
        user.setResetDate(userDTO.getResetDate());
        user.setStripeAccountId(userDTO.getStripeAccountId());
        user.setStripeCustomerId(userDTO.getStripeCustomerId());
        return user;
    }
}