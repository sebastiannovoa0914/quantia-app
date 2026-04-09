package com.quantia.domain.ports.out;

import com.quantia.domain.model.Usuario;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    Usuario guardar(Usuario usuario);
    
    // Este es el que usaremos para validar si el usuario existe antes de loguear
    Optional<Usuario> buscarPorEmail(String email);
}