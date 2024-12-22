package com.example.breakfastorder.service;

import com.example.breakfastorder.dto.OrderDTO;
import com.example.breakfastorder.dto.UserDTO;
import com.example.breakfastorder.entity.Order;
import com.example.breakfastorder.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    public void createOrder(OrderDTO orderDTO);
    public Order getOrderById(Long id);
    public Order updateOrder(OrderDTO orderDTO);
    public void deleteOrderById(Long id);
    public List<Order> findOrders(String username, OrderStatus status, LocalDateTime createdAt, String menuItemName);
}
