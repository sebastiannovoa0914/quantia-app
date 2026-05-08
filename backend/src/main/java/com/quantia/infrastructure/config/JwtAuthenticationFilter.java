package com.quantia.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    
        final String authHeader = request.getHeader("Authorization");
        final String path = request.getServletPath();
    
        // 1. Omitir validación para rutas de autenticación
        if (path.contains("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
    
        // 2. Validar que exista el Bearer Token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
    
        try {
            String jwt = authHeader.substring(7);
            String userEmail = jwtUtil.extractEmail(jwt);
    
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
    
                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    
                    // 3. EXTRACCIÓN DE ROLES (Usando tu método actualizado de JwtUtil)
                    List<String> roles = jwtUtil.extractRoles(jwt);
    
                    // 4. MAPEO DE AUTORIDADES
                    // Transformamos ["ADMINISTRADOR"] en SimpleGrantedAuthority para Spring
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .filter(role -> role != null && !role.isEmpty())
                            .map(SimpleGrantedAuthority::new) 
                            .collect(Collectors.toList());
    
                    // 5. CREACIÓN DEL TOKEN DE SEGURIDAD
                    // Es vital pasar 'authorities' para que el hasAuthority de SecurityConfig funcione
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            authorities 
                    );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 6. ESTABLECER AUTENTICACIÓN
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    // Log de control para ver en la consola de IntelliJ
                    System.out.println("Quantia Portal - Acceso concedido a: " + userEmail + " con roles: " + authorities);
                }
            }
        } catch (Exception e) {
            System.err.println("JWT Filter Error en Quantia Portal: " + e.getMessage());
        }
    
        filterChain.doFilter(request, response);
    }
}