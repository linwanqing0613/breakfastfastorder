package com.example.breakfastorder.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;


@Component
public class JwtTokenProvider {
    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token 的有效時間（以毫秒計，這裡設為 1 小時）
    private final long validityInMilliseconds = 3600000;

    /**
     * 生成 JWT
     * @param phone 用戶號碼
     * @return JWT 字符串
     */
    public String generateToken(String phone) {
        String jti = UUID.randomUUID().toString();
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        log.info("Generating JWT for phone: {} with jti: {}. Token expiration: {}", phone, jti, validity);

        return Jwts.builder()
                .setSubject(phone)
                .setId(jti)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 驗證 JWT
     * @param token 待驗證的 JWT
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e) {
            log.error("Token is expired: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("Signature mismatch: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}",e.getMessage());
            return false;
        }
    }

    /**
     * 從 JWT 中解析用戶名
     * @param token JWT 字符串
     * @return 用戶名
     */
    public String getPhoneNumberFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        log.info("Extracted phone number from token: {}", claims.getSubject());
        return claims.getSubject();
    }

    /**
     * 從 JWT 中解析UUID
     * @param token JWT 字符串
     * @return UUID
     */
    public String getJtiFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getId();
        } catch (Exception e) {
            log.error("Error extracting JTI from token: {}", e.getMessage());
            throw new RuntimeException("Error extracting JTI", e);
        }
    }

    /**
     * 從 JWT 中解析剩餘有效期
     * @param token JWT 字符串
     * @return 剩餘有效期
     */
    public long getExpirationFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return (claims.getExpiration().getTime() - System.currentTimeMillis()) / 1000L;
    }
}
