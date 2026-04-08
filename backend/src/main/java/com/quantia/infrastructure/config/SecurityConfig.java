package com.quantia.infrastructure.config; // Revisa que esta ruta coincida con tus carpetas

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Obligatorio para Postman
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll() // Deja libre todo lo que empiece por /auth
                .anyRequest().authenticated() // Lo demás sí pide clave
            );
        return http.build();
    }
}