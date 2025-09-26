package com.example.demo.security;

import com.example.demo.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails; // ✅ Import
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap; // ✅ Import
import java.util.Map;     // ✅ Import
import java.util.stream.Collectors; // ✅ Import

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyString;

    private SecretKey key;

    // Access token nên có thời hạn ngắn để tăng bảo mật
    private final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 15; // 15 phút
    // Refresh token có thời hạn dài hơn, dùng để lấy access token mới
    private final long REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 7; // 7 ngày

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    //Nhận vào UserDetails để lấy được authorities
    public String generateAccessToken(UserDetails userDetails) {
        // Tạo claims để chứa các thông tin thêm, bao gồm cả vai trò và quyền hạn
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims) // Thêm claims vào token
                .setSubject(userDetails.getUsername()) // Lấy email từ UserDetails
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //Chỉ cần username để tạo Refresh Token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            // Log lỗi ra sẽ tốt hơn
            return false;
        }
    }

    // Giữ lại hàm helper này, nhưng cần đảm bảo CustomUserDetailsService tồn tại
    public static User getLoggedInUser(){
        // Lưu ý: Dòng này yêu cầu UserDetailsService của bạn phải trả về một đối tượng CustomUserDetails
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return userDetails.getUser();
    }
}