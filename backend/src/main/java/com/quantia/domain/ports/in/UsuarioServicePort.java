package com.quantia.domain.ports.in;

import com.quantia.domain.model.Usuario;

public interface UsuarioServicePort {
    Usuario registrarUsuario(Usuario usuario);
    String login(String email, String contrasena);
}