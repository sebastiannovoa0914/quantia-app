package com.quantia.application.services;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.out.UsuarioRepositoryPort;
import com.quantia.infrastructure.config.CustomUserDetailsService;
import com.quantia.infrastructure.config.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock private UsuarioRepositoryPort usuarioRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private CustomUserDetailsService userDetailsService;

    @InjectMocks private UsuarioServiceImpl usuarioService;

    @Test
    void registrarUsuario_DeberiaEncriptarContrasenaYGuardar() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@quantia.com");
        usuario.setContrasena("password123");

        when(passwordEncoder.encode("password123")).thenReturn("hash_encriptado");
        when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = usuarioService.registrarUsuario(usuario);

        // Assert
        assertEquals("hash_encriptado", resultado.getContrasena());
        verify(passwordEncoder, times(1)).encode("password123");
        verify(usuarioRepository, times(1)).guardar(any(Usuario.class));
    }

    @Test
    void login_DeberiaRetornarToken_CuandoCredencialesSonCorrectas() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("test@quantia.com");
        usuario.setContrasena("hash_secreto");

        when(usuarioRepository.buscarPorEmail("test@quantia.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("12345", "hash_secreto")).thenReturn(true);
        when(userDetailsService.loadUserByUsername("test@quantia.com")).thenReturn(mock(UserDetails.class));
        when(jwtUtil.generateToken(any())).thenReturn("TOKEN_VALIDO");

        // Act
        String token = usuarioService.login("test@quantia.com", "12345");

        // Assert
        assertEquals("TOKEN_VALIDO", token);
        verify(jwtUtil, times(1)).generateToken(any());
    }

    @Test
    void login_DeberiaRetornarNull_CuandoContrasenaEsIncorrecta() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setContrasena("hash_correcto");
        when(usuarioRepository.buscarPorEmail("test@quantia.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("wrong_pass", "hash_correcto")).thenReturn(false);

        // Act
        String token = usuarioService.login("test@quantia.com", "wrong_pass");

        // Assert
        assertNull(token);
    }

    @Test
    void login_DeberiaRetornarNull_CuandoUsuarioNoExiste() {
        // Arrange
        when(usuarioRepository.buscarPorEmail("noexiste@quantia.com")).thenReturn(Optional.empty());

        // Act
        String token = usuarioService.login("noexiste@quantia.com", "12345");

        // Assert
        assertNull(token);
    }
}