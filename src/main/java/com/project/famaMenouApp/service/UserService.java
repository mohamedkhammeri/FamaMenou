package com.project.famaMenouApp.service;


import com.project.famaMenouApp.exception.DuplicateEntityException;
import com.project.famaMenouApp.exception.EntityNotFoundException;

import com.project.famaMenouApp.model.dto.UserDTO;
import com.project.famaMenouApp.model.entity.Authority;
import com.project.famaMenouApp.model.entity.User;
import com.project.famaMenouApp.repository.AuthorityRepository;
import com.project.famaMenouApp.repository.UserRepository;
import com.project.famaMenouApp.security.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       AuthorityRepository authorityRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        validateUserDoesNotExist(userDTO);

        User user = UserDTO.toEntity(userDTO);
        // Password is encoded before saving
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        } else {
            throw new IllegalArgumentException("Password cannot be null");
        }

        user.setActivated(false); // New users are inactive by default
        assignDefaultAuthority(user);

        User savedUser = userRepository.save(user);
        log.info("Registered new user with login: {}", user.getLogin());

        return UserDTO.fromEntity(savedUser);
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    UserDTO dto = UserDTO.fromEntity(user);
                    // Never include password in retrieved DTO
                    dto.setPassword(null);
                    return dto;
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDTO dto = UserDTO.fromEntity(user);
                    dto.setPassword(null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        updateUserFields(existingUser, userDTO);

        // Only update password if it's provided in the DTO
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        log.info("Updated user with id: {}", id);

        return UserDTO.fromEntity(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("Deleted user with id: {}", id);
    }

    @Transactional
    public UserDTO activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        user.setActivated(true);
        User activatedUser = userRepository.save(user);
        log.info("Activated user with id: {}", id);

        return UserDTO.fromEntity(activatedUser);
    }

    private void validateUserDoesNotExist(UserDTO userDTO) {
        if (userRepository.findOneByLoginIgnoreCase(userDTO.getLogin()).isPresent()) {
            throw new DuplicateEntityException("Username already exists");
        }
        if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new DuplicateEntityException("Email already exists");
        }
    }

    private void assignDefaultAuthority(User user) {
        Authority userAuthority = authorityRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Authority newAuthority = new Authority();
                    newAuthority.setName("ROLE_USER");
                    return authorityRepository.save(newAuthority);
                });
        user.setAuthorities(Set.of(userAuthority));
    }

    private void updateUserFields(User user, UserDTO userDTO) {
        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(userDTO.getPhoneNumber());
        }
        if (userDTO.getPosition() != null) {
            user.setPosition(userDTO.getPosition());
        }
        if (userDTO.getRangeUser() != null) {
            user.setRangeUser(userDTO.getRangeUser());
        }
        if (userDTO.getImageUrl() != null) {
            user.setImageUrl(userDTO.getImageUrl());
        }
        if (userDTO.getLangKey() != null) {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.isActivated() != user.isActivated()) {
            user.setActivated(userDTO.isActivated());
        }
        // Add other fields as needed
    }

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Changed password for user with id: {}", userId);
    }
    public User getUserByLogin(String login) {
        return userRepository.findOneByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found with login: " + login));
    }
    @Transactional
    @EntityGraph(attributePaths = "authorities")
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }


}