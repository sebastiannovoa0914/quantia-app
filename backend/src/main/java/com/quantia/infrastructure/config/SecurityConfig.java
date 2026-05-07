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
import org.springframework.security.config.Customizer;
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
            .cors(Customizer.withDefaults()) 
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 1. PERMITIR PRE-FLIGHT (CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 2. RUTAS PÚBLICAS (Registro y Login)
                // Se agregan todas las variantes para evitar el 403 "fantasma"
                .requestMatchers("/auth/**", "/api/auth/**").permitAll()
                .requestMatchers("/auth/register", "/auth/login", "/api/auth/register", "/api/auth/login").permitAll()

                // 3. MOVIMIENTOS
                .requestMatchers("/api/movimientos/**")
                    .hasAnyAuthority("ADMINISTRADOR", "CONTADOR")
                
                // 4. PROYECTOS (Lectura y Escritura)
                .requestMatchers(HttpMethod.GET, "/api/proyectos/**")
                    .hasAnyAuthority("ADMINISTRADOR", "PROPIETARIO", "CONTADOR")
                .requestMatchers(HttpMethod.POST, "/api/proyectos/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/api/proyectos/**").hasAuthority("ADMINISTRADOR") // Edición completa
                .requestMatchers(HttpMethod.PATCH, "/api/proyectos/**").hasAuthority("ADMINISTRADOR") // Progreso
                .requestMatchers(HttpMethod.DELETE, "/api/proyectos/**").hasAuthority("ADMINISTRADOR")

                // 5. AGENDA Y HOME
                .requestMatchers("/api/agenda/**").authenticated()
                .requestMatchers("/home/**", "/api/home/**").authenticated()

                // 6. CUALQUIER OTRA PETICIÓN
                .anyRequest().authenticated()
            );

        // Filtro JWT antes del filtro de usuario/contraseña
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200")); 
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control", "Origin", "Accept"));
        config.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}