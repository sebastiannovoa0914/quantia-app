package com.quantia.application.services;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.in.UsuarioServicePort;
import com.quantia.domain.ports.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioServicePort {

    private final UsuarioRepositoryPort usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {
        // Por ahora lo guardamos directo. 
        // Luego configuraremos BCrypt para cifrar la contraseña aquí.
        return usuarioRepository.guardar(usuario);
    }

    @Override
    public String login(String email, String contrasena) {
        // Esto lo programaremos cuando configuremos el JWT.
        return null;
    }
}
