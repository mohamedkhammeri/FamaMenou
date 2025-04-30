package com.project.famaMenouApp.service;

import com.project.famaMenouApp.model.entity.Authority;
import com.project.famaMenouApp.model.entity.User;
import com.project.famaMenouApp.repository.AuthorityRepository;
import com.project.famaMenouApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository,AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository=authorityRepository;
    }


    @Transactional
    public User registerUser(User user)  {
        Authority a=new Authority();
        a.setName("ROLE_USER");
        authorityRepository.save(a);
        userRepository.save(user);
        return  user;

    }


}
