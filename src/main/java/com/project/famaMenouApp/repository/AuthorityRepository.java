package com.project.famaMenouApp.repository;

import com.project.famaMenouApp.model.entity.Authority;
import com.project.famaMenouApp.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByName(String name);
}

