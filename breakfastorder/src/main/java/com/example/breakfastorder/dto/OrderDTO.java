package com.example.breakfastorder.dto;

import com.example.breakfastorder.entity.OrderStatus;

public class OrderDTO {
    private Long id;

    private UserDTO userDTO;

    private OrderStatus status = OrderStatus.PENDING;

    public OrderDTO() {
    }

    public OrderDTO(Long id, UserDTO userDTO, OrderStatus status) {
        this.id = id;
        this.userDTO = userDTO;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
