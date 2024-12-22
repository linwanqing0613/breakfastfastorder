package com.example.breakfastorder.controller;

import com.example.breakfastorder.dto.OrderItemDTO;
import com.example.breakfastorder.dto.ResponseDTO;
import com.example.breakfastorder.entity.OrderItem;
import com.example.breakfastorder.service.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderItem")
public class OrderItemController {
    @Autowired
    private OrderItemService orderItemService;

    // 創建 OrderItem
    @PostMapping
    public ResponseEntity<ResponseDTO<OrderItem>> createOrderItem(@Valid @RequestBody OrderItemDTO orderItemDTO) {
        try {
            OrderItem orderItem = orderItemService.createOrderItem(orderItemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseDTO.success("OrderItem created successfully", orderItem)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }

    // 根據 OrderItem ID 查詢 OrderItem
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<OrderItem>> getOrderItemById(@PathVariable Long id) {
        try {
            OrderItem orderItem = orderItemService.getOrderItemById(id);
            return ResponseEntity.ok().body(
                    ResponseDTO.success("Fetched OrderItem successfully", orderItem)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }

    // 更新 OrderItem
    @PutMapping
    public ResponseEntity<ResponseDTO<OrderItem>> updateOrderItem(@Valid @RequestBody OrderItemDTO orderItemDTO) {
        try {
            OrderItem orderItem = orderItemService.updateOrderItem(orderItemDTO);
            return ResponseEntity.ok().body(
                    ResponseDTO.success("OrderItem updated successfully", orderItem)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }

    // 刪除 OrderItem
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<String>> deleteOrderItem(@PathVariable Long id) {
        try {
            orderItemService.deleteOrderItem(id);
            return ResponseEntity.ok().body(
                    ResponseDTO.success("OrderItem deleted successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }

}
