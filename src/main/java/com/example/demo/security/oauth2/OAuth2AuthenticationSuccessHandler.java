package com.example.demo.security.oauth2;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // Lấy URL frontend từ application.properties để dễ dàng thay đổi
    @Value("${app.oauth2.redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Lấy ra đối tượng User (đã implement UserDetails)
        User user = userRepository.findByEmail(email).orElseGet(() -> createNewUser(email, name));

        // Tạo JWT Token từ đối tượng user (thay vì chỉ từ email)
        String jwt = jwtUtil.generateAccessToken((UserDetails) user);

        // Phần còn lại giữ nguyên
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", jwt)
                .build().toUriString();

        addJwtToCookie(response, jwt);
        clearAuthenticationAttributes(request);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private User createNewUser(String email, String name) {
        User newUser = new User() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }
        };
        newUser.setEmail(email);
        newUser.setFullName(name);
        // Username có thể là email hoặc một chuỗi duy nhất
        newUser.setUsername(email);
        // Mật khẩu có thể để trống hoặc đặt một giá trị ngẫu nhiên vì đây là login OAuth2
        newUser.setPassword("OAUTH2_USER");

        // Gán vai trò mặc định
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Error: Default role 'USER' not found."));
        newUser.setRole(userRole);

        return userRepository.save(newUser);
    }

    private void addJwtToCookie(HttpServletResponse response, String jwt) {
        Cookie jwtCookie = new Cookie("jwt", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60); // 1 ngày
        response.addCookie(jwtCookie);
    }
}