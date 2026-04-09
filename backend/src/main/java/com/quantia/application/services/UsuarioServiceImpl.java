package com.quantia.application.services;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.in.UsuarioServicePort;
import com.quantia.domain.ports.out.UsuarioRepositoryPort;
import com.quantia.infrastructure.config.JwtUtil;
import com.quantia.infrastructure.config.CustomUserDetailsService; // Importante
import org.springframework.security.core.userdetails.UserDetails; // Importante
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioServicePort {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    // Constructor con todas las dependencias necesarias
    public UsuarioServiceImpl(UsuarioRepositoryPort usuarioRepository, 
                              PasswordEncoder passwordEncoder, 
                              JwtUtil jwtUtil,
                              CustomUserDetailsService userDetailsService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        // Encriptamos la contraseña antes de guardar
        String passwordEncriptada = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(passwordEncriptada);
        return usuarioRepository.guardar(usuario);
    }

    @Override
    public String login(String email, String contrasena) {
        // 1. Buscamos al usuario por email usando el puerto de salida
        return usuarioRepository.buscarPorEmail(email)
            .map(usuario -> {
                // 2. Comparamos la contraseña enviada con la de la BD
                if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                    
                    // 3. Cargamos los detalles del usuario (esto incluye los ROLES con el prefijo ROLE_)
                    UserDetails details = userDetailsService.loadUserByUsername(email);
                    
                    // 4. Generamos el Token pasando el objeto UserDetails completo
                    // Esto soluciona el error de tipos y permite que el JWT lleve los permisos
                    return jwtUtil.generateToken(details);
                }
                return null; // Contraseña incorrecta
            })
            .orElse(null); // Usuario no encontrado
    }
}