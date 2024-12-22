package com.example.breakfastorder.dto;

import org.springframework.http.HttpStatus;

public class ResponseDTO<T> {
    private Integer status;
    private String message;
    private T data;

    public ResponseDTO() {}

    public ResponseDTO(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ResponseDTO<T> create(String message, T data) {
        return new ResponseDTO<>(HttpStatus.CREATED.value(), message, data);
    }
    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(HttpStatus.OK.value(), message, data);
    }

    public static <T> ResponseDTO<T> error(String message, T data) {
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), message, data);
    }

    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>(HttpStatus.BAD_REQUEST.value(), message, null);
    }
}
