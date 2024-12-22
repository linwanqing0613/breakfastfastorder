package com.example.breakfastorder.service.impl;

import com.example.breakfastorder.dto.OrderItemDTO;
import com.example.breakfastorder.entity.MenuItem;
import com.example.breakfastorder.entity.Order;
import com.example.breakfastorder.entity.OrderItem;
import com.example.breakfastorder.repository.MenuItemRepository;
import com.example.breakfastorder.repository.OrderItemRepository;
import com.example.breakfastorder.repository.OrderRepository;
import com.example.breakfastorder.service.OrderItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private static final Logger log = LoggerFactory.getLogger(OrderItemServiceImpl.class);
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public OrderItem createOrderItem(OrderItemDTO orderItemDTO) {

        Order order = orderRepository.findById(orderItemDTO.getOrderId())
                .orElseThrow(() ->{
                    log.error("Order with ID {} does not exist", orderItemDTO.getOrderId());
                    return new RuntimeException("Order with ID " + orderItemDTO.getOrderId() + " does not exist.");
                });
        MenuItem menuItem = menuItemRepository.findById(orderItemDTO.getMenuItemId())
                .orElseThrow(() -> {
                    log.error("MenuItem with ID {} does not exist", orderItemDTO.getMenuItemId());
                    return new RuntimeException("MenuItem with ID " + orderItemDTO.getMenuItemId() + " does not exist.");
                });
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(orderItemDTO.getQuantity());
        orderItem = orderItemRepository.save(orderItem);
        log.info("OrderItem with ID {} created successfully", orderItem.getId());
        return orderItem;
    }

    @Override
    public OrderItem getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("OrderItem with ID {} not found", id);
                    return new RuntimeException("OrderItem with ID " + id + " not found");
                });

        log.info("Fetched OrderItem with ID {}", id);
        return orderItem;
    }

    @Override
    public OrderItem updateOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem existingOrderItem = orderItemRepository.findById(orderItemDTO.getId())
                .orElseThrow(() -> {
                    log.error("OrderItem with ID {} not found", orderItemDTO.getId());
                    return new RuntimeException("OrderItem with ID " + orderItemDTO.getId() + " not found");
                });
        MenuItem menuItem = menuItemRepository.findById(orderItemDTO.getMenuItemId())
                .orElseThrow(() -> {
                    log.error("MenuItem with ID {} does not exist", orderItemDTO.getMenuItemId());
                    return new RuntimeException("MenuItem with ID " + orderItemDTO.getMenuItemId() + " does not exist.");
                });
        existingOrderItem.setQuantity(orderItemDTO.getQuantity());
        existingOrderItem.setMenuItem(menuItem);

        orderItemRepository.save(existingOrderItem);
        log.info("OrderItem with ID {} updated successfully", existingOrderItem.getId());

        return existingOrderItem;
    }

    @Override
    public void deleteOrderItem(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("OrderItem with ID {} not found", id);
                    return new RuntimeException("OrderItem with ID " + id + " not found");
                });
        orderItemRepository.delete(orderItem);
        log.info("OrderItem with ID {} deleted successfully", id);
    }
}
