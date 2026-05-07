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

        // 1. OBTENER LA RUTA DE LA PETICIÓN
        String path = request.getServletPath();

        // 2. EXCEPCIÓN CRÍTICA PARA RUTAS PÚBLICAS
        // Si la petición va a /auth (registro o login), dejamos pasar sin validar JWT
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        // 3. VALIDAR PRESENCIA DEL TOKEN BEARER
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = authHeader.substring(7);
            String userEmail = jwtUtil.extractEmail(jwt);

            // 4. PROCESAR AUTENTICACIÓN SI EL EMAIL ES VÁLIDO Y NO HAY SESIÓN PREVIA
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    
                    // Extraemos los roles del claim "roles"
                    List<String> roles = jwtUtil.extractClaim(jwt, claims -> claims.get("roles", List.class));

                    // Mapeo de autoridades asegurando el prefijo ROLE_ para que coincida con SecurityConfig
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, 
                            null, 
                            authorities 
                    );
                    
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Establecer la autenticación en el contexto global de Spring
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    System.out.println("Filtro JWT: Autenticación exitosa para " + userEmail);
                }
            }
        } catch (Exception e) {
            // Si el token está malformado o expirado, no detenemos la cadena,
            // simplemente no autenticamos y Spring Security lanzará el 403 después si la ruta es privada.
            System.err.println("Error procesando JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}