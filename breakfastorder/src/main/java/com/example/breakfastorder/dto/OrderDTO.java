package com.example.breakfastorder.dto;

import com.example.breakfastorder.entity.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class OrderDTO {
    private Long id;

    @NotBlank(message = "User is required")
    private UserDTO userDTO;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive value")
    private BigDecimal totalPrice;

    private OrderStatus status = OrderStatus.PENDING;

    public OrderDTO() {
    }

    public OrderDTO(Long id, UserDTO userDTO, BigDecimal totalPrice, OrderStatus status) {
        this.id = id;
        this.userDTO = userDTO;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return userDTO;
    }

    public void setUser(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
