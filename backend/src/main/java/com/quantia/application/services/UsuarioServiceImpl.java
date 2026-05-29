package com.quantia.application.services;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.in.UsuarioServicePort;
import com.quantia.domain.ports.out.UsuarioRepositoryPort;
import com.quantia.infrastructure.config.JwtUtil;
import com.quantia.infrastructure.config.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioServicePort {

    private final UsuarioRepositoryPort usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

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
        logger.info("Iniciando registro de usuario: {}", usuario.getEmail());
        try {
            String passwordEncriptada = passwordEncoder.encode(usuario.getContrasena());
            usuario.setContrasena(passwordEncriptada);
            Usuario guardado = usuarioRepository.guardar(usuario);
            logger.info("Usuario registrado exitosamente con ID: {}", guardado.getId());
            return guardado;
        } catch (Exception e) {
            logger.error("Error al registrar el usuario: {}", usuario.getEmail(), e);
            throw e;
        }
    }

    @Override
    public String login(String email, String contrasena) {
        logger.info("Intento de login para el usuario: {}", email);
        return usuarioRepository.buscarPorEmail(email)
            .map(usuario -> {
                if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                    UserDetails details = userDetailsService.loadUserByUsername(email);
                    logger.info("Login exitoso para usuario: {}", email);
                    return jwtUtil.generateToken(details);
                } else {
                    logger.warn("Login fallido: Contraseña incorrecta para el usuario: {}", email);
                    return null;
                }
            })
            .orElseGet(() -> {
                logger.warn("Login fallido: Usuario no encontrado con email: {}", email);
                return null;
            });
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        logger.debug("Buscando usuario por email: {}", email);
        return usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> {
                    logger.error("Error al buscar usuario: no existe el email {}", email);
                    return new RuntimeException("Usuario no encontrado con email: " + email);
                });
    }
}