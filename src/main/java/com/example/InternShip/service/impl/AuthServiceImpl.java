package com.example.InternShip.service.impl;

import com.example.InternShip.dto.request.LoginRequest;
import com.example.InternShip.dto.request.RefreshTokenRequest;
import com.example.InternShip.dto.request.RegisterRequest;
import com.example.InternShip.dto.response.TokenResponse;
import com.example.InternShip.entity.PendingUser;
import com.example.InternShip.entity.User;
import com.example.InternShip.exception.ErrorCode;
import com.example.InternShip.repository.PendingUserRepository;
import com.example.InternShip.repository.UserRepository;
import com.example.InternShip.service.AuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PendingUserRepository pendingUserRepository;
    private final PendingUserServiceImpl pendingUserService;
    @Value("${jwt.singerKey}")
    private String singerKey;

    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException(ErrorCode.USERNAME_EXISTED.getMessage());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(ErrorCode.EMAIL_EXISTED.getMessage());
        }

        String token = UUID.randomUUID().toString();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        PendingUser pendingUser = modelMapper.map(request, PendingUser.class);
        pendingUser.setPassword(encoder.encode(request.getPassword()));
        pendingUser.setToken(token);
        pendingUser.setExpiryDate(LocalDateTime.now().plusMinutes(20));
        pendingUserRepository.save(pendingUser);

        String verifyLink = "http://localhost:8082/api/v1/pendingUser/verify?token=" + token;
        pendingUserService.sendVerification(request.getEmail(), verifyLink);
    }

    public TokenResponse login(LoginRequest request) throws JOSEException {
        User user = userRepository.findByUsernameOrEmail(request.getIdentifier())
                .orElseThrow(() -> new RuntimeException(ErrorCode.UNAUTHENTICATED.getMessage()));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean auth = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!auth) {
            throw new RuntimeException(ErrorCode.UNAUTHENTICATED.getMessage());
        }

        if (user.isActive() == false) {
            throw new RuntimeException(ErrorCode.USER_INACTIVE.getMessage());
        }

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }

    public User getUserLogin() {
        SecurityContext context = SecurityContextHolder.getContext();
        String username = context.getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(ErrorCode.USER_NOT_EXISTED.getMessage()));
    }

    private String generateAccessToken(User user) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("codegym")
                .issueTime(new Date())
                .claim("scope", user.getRole())
                .claim("isActive", user.isActive())
                .claim("isRefreshToken", false)
                .expirationTime(Date.from(Instant.now().plus(100, ChronoUnit.MINUTES)))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        jwsObject.sign(new MACSigner(singerKey));
        return jwsObject.serialize();
    }

    private String generateRefreshToken(User user) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("codegym")
                .issueTime(new Date())
                .claim("isRefreshToken", true)
                .expirationTime(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        jwsObject.sign(new MACSigner(singerKey));
        return jwsObject.serialize();
    }

    public TokenResponse refreshToken(RefreshTokenRequest request) throws JOSEException, ParseException {
        String token = request.getRefreshToken();

        JWSVerifier verifier = new MACVerifier(singerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean verified = signedJWT.verify(verifier);
        boolean expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date());
        boolean isRefreshToken = signedJWT.getJWTClaimsSet().getBooleanClaim("isRefreshToken");

        if (!isRefreshToken || !expiryTime || !verified) {
            throw new RuntimeException(ErrorCode.TOKEN_INVALID.getMessage());
        }

        User user = userRepository.findByUsername(signedJWT.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new RuntimeException(ErrorCode.USER_NOT_EXISTED.getMessage()));

        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken);
    }
}
