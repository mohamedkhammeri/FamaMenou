package com.project.famaMenouApp.repository;

import com.project.famaMenouApp.model.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByLogin(String login);


    Optional<User> findOneByLoginIgnoreCase(String login);

    Optional<User> findOneByEmailIgnoreCase(String email);
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByLogin(String login);
    boolean existsByLoginIgnoreCase(String login);

    boolean existsByEmailIgnoreCase(String email);

    // Additional methods if needed
    Optional<User> findByResetKey(String resetKey);

    Optional<User> findByActivationKey(String activationKey);
}