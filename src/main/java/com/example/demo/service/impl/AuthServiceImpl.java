package com.example.demo.service.impl;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.entity.EmailToken;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.EmailTokenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.AuthService;
import com.example.demo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailTokenRepository emailTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Override
    public LoginResponse login(LoginRequest request){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword()));

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            User user = userDetails.getUser();
            String role = user.getRole().getRoleName();
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

            return new LoginResponse(accessToken, refreshToken);
        }catch (BadCredentialsException ex){
            throw new AppException(ErrorCode.LOGIN_FAILED);
        }catch (DisabledException ex){
            throw new AppException(ErrorCode.ACCOUNT_DISABLED);
        }
    }

    @Override
    public void register(RegisterRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.DUPLICATE_USERNAME);
        }
        if (userRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.DUPLICATE_EMAIL);
        }

        String token = UUID.randomUUID().toString();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        EmailToken emailtoken = modelMapper.map(request, EmailToken.class);
        emailtoken.setToken(token);
        emailtoken.setPassword(encoder.encode(request.getPassword()));
        emailtoken.setExpiryDate(LocalDateTime.now().plusMinutes(20));
        emailTokenRepository.save(emailtoken);

        String verifyLink = "http://localhost:8081/api/email/verify?token=" + token;
        emailService.sendVerification(request.getEmail(),verifyLink);
    }
}
