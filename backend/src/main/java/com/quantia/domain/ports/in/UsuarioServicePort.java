package com.quantia.domain.ports.in;

import com.quantia.domain.model.Usuario;

public interface UsuarioServicePort {
    Usuario registrarUsuario(Usuario usuario);
    
    String login(String email, String contrasena);

    // Nuevo método necesario para obtener la info que mostraremos en el Home
    Usuario buscarPorEmail(String email);
}