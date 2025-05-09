package com.project.famaMenouApp.service;

import com.project.famaMenouApp.exception.UserNotActivatedException;
import com.project.famaMenouApp.model.dto.UserDTO;
import com.project.famaMenouApp.model.entity.User;
import com.project.famaMenouApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator = new EmailValidator();

    public CUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        User user = findUserByLoginOrEmail(login)
                .orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found"));

        validateUserActivation(user);

        return createSpringSecurityUser(user);
    }

    @Transactional
    public UserDTO loadUserDTOByUsername(String login) {
        User user = findUserByLoginOrEmail(login)
                .orElseThrow(() -> new UsernameNotFoundException("User " + login + " was not found"));
        return UserDTO.fromEntity(user);
    }

    private Optional<User> findUserByLoginOrEmail(String login) {
        if (emailValidator.isValid(login, null)) {
            return userRepository.findOneByEmailIgnoreCase(login);
        }
        return userRepository.findOneByLoginIgnoreCase(login.toLowerCase(Locale.ENGLISH));
    }

    private void validateUserActivation(User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + user.getLogin() + " was not activated");
        }
        if (!user.isPhoneActivated() && user.getPhoneNumber() != null) {
            throw new UserNotActivatedException("User " + user.getLogin() + " phone number not verified");
        }
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.isActivated(),
                true,  // account non-expired
                true,  // credentials non-expired
                true,  // account non-locked
                grantedAuthorities
        );
    }
}