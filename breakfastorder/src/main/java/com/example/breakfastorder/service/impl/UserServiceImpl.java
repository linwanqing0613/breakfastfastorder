package com.example.breakfastorder.service.impl;

import com.example.breakfastorder.dto.LoginDTO;
import com.example.breakfastorder.dto.UserDTO;
import com.example.breakfastorder.entity.User;
import com.example.breakfastorder.repository.UserRepository;
import com.example.breakfastorder.security.InvalidTokenException;
import com.example.breakfastorder.security.JwtTokenProvider;
import com.example.breakfastorder.service.JwtBlackListService;
import com.example.breakfastorder.service.UserService;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtBlackListService jwtBlackListService;

    @Override
    public void register(UserDTO userDTO) {
        if (userRepository.existsByPhone(userDTO.getPhone())) {
            throw new RuntimeException("Phone number already registered");
        }
        User user = new User();
        user.setPhone(userDTO.getPhone());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(user);
    }

    @Override
    public String login(LoginDTO loginDTO) {
        User user = userRepository.findByPhone(loginDTO.getPhone())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        String token = jwtTokenProvider.generateToken(user.getPhone());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(loginDTO.getPhone(), token, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return token;
    }

    @Override
    public void logout() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();

        if (!StringUtils.hasText(token)) {
            log.warn("logout: Invalid token - Token is null or empty");
            SecurityContextHolder.clearContext();
            throw new InvalidTokenException("logout: Token is required for logout");
        }
        try {

            long expiration = jwtTokenProvider.getExpirationFromToken(token);
            String jti = jwtTokenProvider.getJtiFromToken(token);

            if (expiration <= 0) {
                log.warn("logout: Invalid token expiration - Token already expired");
                SecurityContextHolder.clearContext();
                throw new InvalidTokenException("logout: Token already expired");
            }

            jwtBlackListService.addToBlackList(jti, expiration);
            log.info("logout: Token added to blacklist. JTI: {}, Expiration: {} seconds", jti, expiration);
            SecurityContextHolder.clearContext();
        } catch (JwtException e) {
            log.error("logout: Failed to parse token. Error: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            throw new InvalidTokenException("logout: Failed to parse token");
        } catch (Exception e) {
            log.error("logout: Unexpected error occurred during logout. Error: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            throw new RuntimeException("logout: Unexpected error occurred");
        }
    }

    @Override
    public void deleteUserAccount(LoginDTO loginDTO) {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("deleteUserAccount: Invalid token");
        }
        String phone = jwtTokenProvider.getPhoneNumberFromToken(token);

        if(phone.equals(loginDTO.getPhone())){
            throw new RuntimeException("Invalid phone number");
        }
        User user = userRepository.findByPhone(loginDTO.getPhone())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        userRepository.delete(user);
        log.info("User deleted: {}", loginDTO.getPhone());
    }
}
