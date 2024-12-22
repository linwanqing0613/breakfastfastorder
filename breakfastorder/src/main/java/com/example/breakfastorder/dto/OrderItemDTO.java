package com.example.breakfastorder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Long id;

    @NotNull(message = "Order Id is required")
    private Long orderId;

    @NotNull(message = "MenuItem Id is required")
    private Long menuItemId;

    @NotNull(message = "Quantity Id is required")
    private Integer quantity;

    public OrderItemDTO() {
    }

    public OrderItemDTO(Long id, Long orderId, Long menuItemId, Integer quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
