package com.example.breakfastorder.controller;

import com.example.breakfastorder.dto.MenuItemDTO;
import com.example.breakfastorder.dto.OrderDTO;
import com.example.breakfastorder.dto.ResponseDTO;
import com.example.breakfastorder.entity.Order;
import com.example.breakfastorder.entity.OrderStatus;
import com.example.breakfastorder.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseDTO<Void>> createMenuItem(@Valid @RequestBody OrderDTO orderDTO){
        try {
            orderService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseDTO.create("Menu item created successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @GetMapping("/{id}")
    public  ResponseEntity<ResponseDTO<Order>> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id);
            return ResponseEntity.ok().body(
                    ResponseDTO.success("Order retrieved successfully", order)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @PutMapping
    public ResponseEntity<ResponseDTO<Order>> updateOrder(@Valid @RequestBody OrderDTO orderDTO) {
        try {
            Order order = orderService.updateOrder(orderDTO);
            return ResponseEntity.ok().body(
                    ResponseDTO.success("Order updated successfully", order)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteOrderById(@PathVariable Long id) {
        try {
            orderService.deleteOrderById(id);
            return ResponseEntity.ok().body(
                    ResponseDTO.success("Order deleted successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @GetMapping
    public ResponseEntity<ResponseDTO<List<Order>>> findOrders(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) LocalDateTime createdAt,
            @RequestParam(required = false) String menuItemName) {
        try {
            List<Order> orders = orderService.findOrders(username, status, createdAt, menuItemName);
            return ResponseEntity.ok().body(
                    ResponseDTO.success("Orders retrieved successfully", orders)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
}
