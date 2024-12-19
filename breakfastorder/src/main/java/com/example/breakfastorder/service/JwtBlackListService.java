package com.example.breakfastorder.service;

public interface JwtBlackListService {
    public void addToBlackList(String jti, long expirationSeconds);
    public boolean isTokenBlackList(String jti);
}
