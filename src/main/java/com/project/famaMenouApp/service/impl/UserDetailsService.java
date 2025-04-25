package com.project.famaMenouApp.service.impl;

import com.project.famaMenouApp.exception.UserNotActivatedException;
import com.project.famaMenouApp.model.entity.User;
import com.project.famaMenouApp.repository.UserRepository;
import com.project.famaMenouApp.service.interfaces.IUserDetailsService;
import jakarta.transaction.Transactional;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class UserDetailsService implements IUserDetailsService {

    private final UserRepository userRepository;
    private final EmailValidator emailValidator = new EmailValidator();

    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        if (emailValidator.isValid(login, null)) {
            return userRepository.findOneByEmailIgnoreCase(login)
                    .map(this::createSpringSecurityUser)
                    .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository.findOneByLogin(lowercaseLogin)
                .map(this::createSpringSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + user.getLogin() + " was not activated");
        }

        if (!user.isPhoneActivated() && user.getPhoneNumber() != null) {
            throw new UserNotActivatedException("User " + user.getLogin() + " phone number not verified");
        }

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
