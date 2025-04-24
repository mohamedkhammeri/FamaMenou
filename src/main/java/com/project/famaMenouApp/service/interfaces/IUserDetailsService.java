package com.project.famaMenouApp.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserDetailsService {
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException;
}
