package com.project.AuctionPlatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF (only for development)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll() // Allow access to /users
                        .anyRequest().authenticated() // Require authentication for other endpoints
                )
                .httpBasic(Customizer.withDefaults()); // Enable Basic Authentication
        return http.build();
    }
}
