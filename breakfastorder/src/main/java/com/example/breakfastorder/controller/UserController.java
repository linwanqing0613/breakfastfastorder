package com.example.breakfastorder.controller;

import com.example.breakfastorder.dto.LoginDTO;
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
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO){
        try {
            userService.register(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO){
        try {
            String token = userService.login(loginDTO);
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        try {
            userService.logout();
            return ResponseEntity.ok("User logged out successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{phone}")
    public ResponseEntity<String> deleteUserAccount(@RequestBody LoginDTO loginDTO) {
        try {
            userService.deleteUserAccount(loginDTO);
            return ResponseEntity.ok("User account deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
