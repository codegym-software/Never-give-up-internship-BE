package com.example.InternShip.config;

import com.example.InternShip.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${jwt.singerKey}")
    private String singerKey;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults -> {})
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/pendingUser/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/pendingUserForgetPassword/**").permitAll()
                        .anyRequest().authenticated()
                );


        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
                );
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
    
    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec spec = new SecretKeySpec(singerKey.getBytes(),"HS512");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
                .withSecretKey(spec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();

        OAuth2TokenValidator<Jwt> defaultValidator = JwtValidators.createDefault();
        OAuth2TokenValidator<Jwt> customValidator = token -> {
            Boolean isActive = token.getClaimAsBoolean("isActive");
            if (!Boolean.TRUE.equals(isActive)) {
                return OAuth2TokenValidatorResult.failure(
                        new OAuth2Error("invalid_token", ErrorCode.USER_INACTIVE.getMessage(), null)
                );
            }
            return OAuth2TokenValidatorResult.success();
        };

        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(defaultValidator, customValidator));
        return jwtDecoder;
    }
}

/*
 *  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authenticationProvider(new DaoAuthenticationProvider() {
                    {
                        setUserDetailsService(superAdminDetailsService());
                        setPasswordEncoder(passwordEncoder());
                    }
                })
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/register", "/login", "/css/**").permitAll()
                // USER: chỉ xem danh sách
                .requestMatchers("/users").hasAnyRole("USER", "ADMIN", "SUPER_ADMIN")
                // ADMIN: CRUD users
                .requestMatchers("/users/create", "/users/{id}/edit", "/users/{id}/update").hasRole("ADMIN")
                .requestMatchers("/users/{id}").hasRole("ADMIN") // DELETE
                .requestMatchers("/users").hasRole("ADMIN") // POST (create)
                // SUPER_ADMIN: quản lý accounts
                .requestMatchers("/admin/accounts/**").hasRole("SUPER_ADMIN")
                .requestMatchers("/admin/dashboard").hasRole("SUPER_ADMIN")
                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/users", true)
                .permitAll()
                )
                .logout((logout) -> logout
                .logoutSuccessUrl("/login")
                .permitAll());

        return http.build();
    }
 * 
 * 
 */