package com.example.breakfastorder.repository;

import com.example.breakfastorder.entity.MenuItem;
import com.example.breakfastorder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean  existsByPhone(String phone);
    Optional<User> findByPhone(String phone);
}
