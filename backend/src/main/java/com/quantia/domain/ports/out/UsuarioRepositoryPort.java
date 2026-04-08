package com.quantia.domain.ports.out;

import com.quantia.domain.model.Usuario;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorEmail(String email);
}