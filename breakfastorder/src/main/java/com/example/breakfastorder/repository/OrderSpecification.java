package com.example.breakfastorder.repository;

import com.example.breakfastorder.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {
    public static Specification<Order> searchByCriteria(
            String username, OrderStatus status, LocalDateTime createdAt, String menuItemName) {

        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // Filter by username
            if (username != null && !username.isEmpty()) {
                Join<Order, User> userJoin = root.join("user");
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(userJoin.get("username"), "%" + username + "%"));
            }

            // Filter by status
            if (status != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("status"), status));
            }

            // Filter by createdAt
            if (createdAt != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("createdAt"), createdAt));
            }

            // Filter by menuItemName
            if (menuItemName != null && !menuItemName.isEmpty()) {
                Join<Order, OrderItem> orderItemJoin = root.join("orderItems");
                Join<OrderItem, MenuItem> menuItemJoin = orderItemJoin.join("menuItem");

                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(menuItemJoin.get("name"), "%" + menuItemName + "%"));
            }

            return predicate;
        };
    }
}
