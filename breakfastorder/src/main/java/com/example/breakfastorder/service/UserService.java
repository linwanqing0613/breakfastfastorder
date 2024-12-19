package com.example.breakfastorder.service;

import com.example.breakfastorder.dto.LoginDTO;
import com.example.breakfastorder.dto.UserDTO;

public interface UserService {
    public void register(UserDTO userDTO);
    public String login(LoginDTO loginDTO);
    public void logout();
    public void deleteUserAccount(LoginDTO loginDTO);
}
