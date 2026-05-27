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
            .requestMatchers(HttpMethod.POST, "/api/funciones/propietario").hasAuthority("ADMINISTRADOR")
            // 1. PERMITIR PRE-FLIGHT (CORS) - Vital para que el navegador autorice PUT/DELETE
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            // 1.5 RUTAS DE SWAGGER / OPENAPI (Públicas para documentación y pruebas)
            .requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/webjars/**"
            ).permitAll()

            .requestMatchers("/api/contabilidad/**").hasAnyAuthority("ADMINISTRADOR", "CONTADOR")
            

            // 2. RUTAS PÚBLICAS (Registro y Login)
            .requestMatchers("/auth/**", "/api/auth/**").permitAll()

            // 3. MOVIMIENTOS
            .requestMatchers("/api/movimientos/**")
                .hasAnyAuthority("ADMINISTRADOR", "CONTADOR")
            
            // 4. PROYECTOS (Jerarquía corregida para Edición)
            // Permitimos la lectura (lista y por ID) a los roles autorizados
            .requestMatchers(HttpMethod.GET, "/api/proyectos/**")
                .hasAnyAuthority("ADMINISTRADOR", "PROPIETARIO", "CONTADOR")
            
            // Restringimos creación, edición completa, progreso y eliminación solo a ADMIN
            .requestMatchers(HttpMethod.POST, "/api/proyectos/**").hasAuthority("ADMINISTRADOR")
            .requestMatchers(HttpMethod.PUT, "/api/proyectos/**").hasAuthority("ADMINISTRADOR")
            .requestMatchers(HttpMethod.PATCH, "/api/proyectos/**").hasAuthority("ADMINISTRADOR")
            .requestMatchers(HttpMethod.DELETE, "/api/proyectos/**").hasAuthority("ADMINISTRADOR")

            // 5. AGENDA Y HOME
            .requestMatchers("/api/agenda/**").authenticated()
            .requestMatchers("/home/**", "/api/home/**").authenticated()

            // 6. CUALQUIER OTRA PETICIÓN
            .anyRequest().authenticated()
        );

    // Inserción del filtro JWT antes del filtro de usuario/contraseña de Spring
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
        
        // CORRECCIÓN: Lista de Strings limpia, sin comillas internas
        config.setAllowedOrigins(List.of(
            "http://localhost",
            "http://127.0.0.1",
            "http://localhost:4200"
        )); 
        
        // Métodos permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Headers permitidos (Solo una vez y bien completo)
        config.setAllowedHeaders(List.of(
            "Authorization", 
            "Content-Type", 
            "Cache-Control", 
            "Origin", 
            "Accept", 
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        config.setAllowCredentials(true);
        config.setMaxAge(3600L); 
    
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}