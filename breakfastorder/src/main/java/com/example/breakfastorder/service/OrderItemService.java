package com.example.breakfastorder.service;

import com.example.breakfastorder.dto.OrderItemDTO;
import com.example.breakfastorder.entity.OrderItem;

public interface OrderItemService {
    OrderItem createOrderItem(OrderItemDTO orderItemDTO);
    OrderItem getOrderItemById(Long id);
    OrderItem updateOrderItem(OrderItemDTO orderItemDTO);
    void deleteOrderItem(Long id);
}
