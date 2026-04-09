package com.quantia.application.services;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.in.UsuarioServicePort;
import com.quantia.domain.ports.out.UsuarioRepositoryPort;
import com.quantia.infrastructure.config.JwtUtil;
import com.quantia.infrastructure.config.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioServicePort {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

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
        String passwordEncriptada = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(passwordEncriptada);
        return usuarioRepository.guardar(usuario);
    }

    @Override
    public String login(String email, String contrasena) {
        return usuarioRepository.buscarPorEmail(email)
            .map(usuario -> {
                if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                    UserDetails details = userDetailsService.loadUserByUsername(email);
                    return jwtUtil.generateToken(details);
                }
                return null;
            })
            .orElse(null);
    }

    // Este es el método que falta y por el cual te marca error arriba
    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }
}