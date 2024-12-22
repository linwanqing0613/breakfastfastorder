package com.example.breakfastorder.service.impl;

import com.example.breakfastorder.dto.LoginDTO;
import com.example.breakfastorder.dto.OrderDTO;
import com.example.breakfastorder.dto.UserDTO;
import com.example.breakfastorder.entity.Order;
import com.example.breakfastorder.entity.OrderStatus;
import com.example.breakfastorder.entity.User;
import com.example.breakfastorder.repository.OrderRepository;
import com.example.breakfastorder.repository.OrderSpecification;
import com.example.breakfastorder.repository.UserRepository;
import com.example.breakfastorder.security.JwtTokenProvider;
import com.example.breakfastorder.service.OrderService;
import com.example.breakfastorder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void createOrder(OrderDTO orderDTO) {
        User currentUser = validateTokenAndGetUser();
        if(orderDTO.getId() != null && orderRepository.existsById(orderDTO.getId())){
            throw new RuntimeException("Order with ID " + orderDTO.getId() + " already exists.");
        }

        if (orderDTO.getUser() == null) {
            orderDTO.setUser(convertToUserDTO(currentUser));
        } else {
            if (!orderDTO.getUser().getPhone().equals(currentUser.getPhone())) {
                throw new RuntimeException("User mismatch: Cannot create order for a different user.");
            }
            if(!passwordEncoder.matches(orderDTO.getUser().getPassword(), currentUser.getPassword())){
                throw new RuntimeException("Authentication failed: Invalid password.");
            }
            orderDTO.setId(currentUser.getId());
        }
        Order order = convertToEntity(orderDTO);
        orderRepository.save(order);
        log.info("Order created successfully: {}", order);
    }

    @Override
    public Order getOrderById(Long id) {
        User currentUser = validateTokenAndGetUser();
        log.info("User with ID {} is attempting to retrieve order with ID {}", currentUser.getId(), id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order with ID " + id + " not found."));

        if (!order.getUser().getId().equals(currentUser.getId())) {
            log.warn("User with ID {} is not authorized to view order with ID {}", currentUser.getId(), id);
            throw new RuntimeException("You are not authorized to view this order.");
        }

        log.info("User with ID {} successfully retrieved order with ID {}", currentUser.getId(), id);
        return order;
    }

    @Override
    public Order updateOrder(OrderDTO orderDTO) {
        User currentUser = validateTokenAndGetUser();
        log.info("User with ID {} is attempting to update order with ID {}", currentUser.getId(), orderDTO.getId());

        Order order = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new RuntimeException("Order with ID " + orderDTO.getId() + " not found."));

        if (!order.getUser().getId().equals(currentUser.getId())) {
            log.warn("User with ID {} is not authorized to update order with ID {}", currentUser.getId(), orderDTO.getId());
            throw new RuntimeException("You are not authorized to update this order.");
        }
        updateEntityFromDTO(order, orderDTO);
        orderRepository.save(order);
        log.info("User with ID {} successfully updated order with ID {}", currentUser.getId(), orderDTO.getId());
        return order;
    }

    @Override
    public void deleteOrderById(Long id) {
        User currentUser = validateTokenAndGetUser();
        log.info("User with ID {} is attempting to delete order with ID {}", currentUser.getId(), id);

        if (!orderRepository.existsById(id)) {
            log.warn("User with ID {} attempted to delete a non-existing order with ID {}", currentUser.getId(), id);
        }
        Order order = orderRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Order with ID " + id + " not found."));

        if (!order.getUser().getId().equals(currentUser.getId())) {
            log.warn("User with ID {} is not authorized to delete order with ID {}", currentUser.getId(), id);
            throw new RuntimeException("You are not authorized to delete this order.");
        }
        orderRepository.deleteById(id);
        log.info("User with ID {} successfully deleted order with ID {}", currentUser.getId(), id);
    }

    @Override
    public List<Order> findOrders(String username, OrderStatus status, LocalDateTime createdAt, String menuItemName) {
        User currentUser = validateTokenAndGetUser();
        log.info("User with ID {} is searching for orders", currentUser.getId());

        Specification<Order> spec = OrderSpecification.searchByCriteria(username, status, createdAt, menuItemName);
        List<Order> orders = orderRepository.findAll(spec);

        if (orders.isEmpty()) {
            log.warn("User with ID {} found no orders matching the criteria", currentUser.getId());
        } else {
            log.info("User with ID {} found {} orders matching the criteria", currentUser.getId(), orders.size());
        }
        return orders;
    }

    private Order convertToEntity(OrderDTO orderDTO) {
        Order order = new Order();
        updateEntityFromDTO(order, orderDTO);
        return order;
    }
    private void updateEntityFromDTO(Order order, OrderDTO orderDTO){
        if(orderDTO.getTotalPrice() != null){
            order.setTotalPrice(orderDTO.getTotalPrice());
        }
        if(orderDTO.getStatus() != null){
            order.setStatus(orderDTO.getStatus());
        }
    }
    private User validateTokenAndGetUser() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Invalid token: Token is missing.");
        }
        String phone = jwtTokenProvider.getPhoneNumberFromToken(token);
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found for token: " + phone));
    }
    private UserDTO convertToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getPhone(), user.getPassword());
    }
}
