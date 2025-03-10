package com.example.blendish.domain.user.repository;

import com.example.blendish.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserId(String userId);
    User findByUserId(String userId);
}
