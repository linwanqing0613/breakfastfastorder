package com.example.breakfastorder.repository;


import com.example.breakfastorder.entity.Order;
import com.example.breakfastorder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findByUser(User user);
    List<Order> findByStatus(String status);
    List<Order> findByUserAndStatus(User user, String status);
}
