package com.example.breakfastorder.controller;

import com.example.breakfastorder.dto.LoginDTO;
import com.example.breakfastorder.dto.ResponseDTO;
import com.example.breakfastorder.dto.UserDTO;
import com.example.breakfastorder.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<Void>> register(@RequestBody UserDTO userDTO){
        try {
            userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ResponseDTO.create("User registered successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<String>> login(@RequestBody LoginDTO loginDTO){
        try {
            String token = userService.login(loginDTO);
            return ResponseEntity.ok(
                    ResponseDTO.success("User logged out successfully", token)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<ResponseDTO<Void>> logout() {
        try {
            userService.logout();
            return ResponseEntity.ok(
                    ResponseDTO.success("User logged out successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
    @DeleteMapping("/{phone}")
    public ResponseEntity<ResponseDTO<Void>> deleteUserAccount(@RequestBody LoginDTO loginDTO) {
        try {
            userService.deleteUserAccount(loginDTO);
            return ResponseEntity.ok(
                    ResponseDTO.success("User account deleted successfully", null)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(e.getMessage()));
        }
    }
}
