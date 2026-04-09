package com.quantia.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1. Público: Registro y Login
                .requestMatchers("/auth/**").permitAll()
                
                // 2. NUEVO: Ruta de Bienvenida/Home
                // Permitimos que CUALQUIER rol entre aquí, el controlador se encarga de saludar
                .requestMatchers("/home/**").authenticated()
                
                // 3. Movimientos: Solo Admin y Contador
                .requestMatchers("/movimientos", "/movimientos/**")
                    .hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_CONTADOR")
                
                // 4. Proyectos: Lectura para Admin/Propietario, Escritura solo Admin
                .requestMatchers(HttpMethod.GET, "/proyectos", "/proyectos/**")
                    .hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_PROPIETARIO")
                .requestMatchers(HttpMethod.POST, "/proyectos", "/proyectos/**")
                    .hasAuthority("ROLE_ADMINISTRADOR")
                
                // 5. Bloqueo por defecto
                .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}