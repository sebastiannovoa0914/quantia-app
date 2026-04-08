package com.quantia.infrastructure.adapters.out.persistence;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final JpaUsuarioRepository jpaRepository;

    public UsuarioRepositoryAdapter(JpaUsuarioRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setNombre(usuario.getNombre());
        entity.setEmail(usuario.getEmail());
        entity.setContrasena(usuario.getContrasena());
        entity.setRol(Rol.valueOf(usuario.getRol()));
        entity.setActivo(usuario.isActivo());
        
        UsuarioEntity savedEntity = jpaRepository.save(entity);
        usuario.setId(savedEntity.getIdUsuario());
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email)
            .map(entity -> new Usuario(
                entity.getIdUsuario(),
                entity.getNombre(),
                entity.getEmail(),
                entity.getContrasena(),
                entity.getRol().name(),
                entity.isActivo()
            ));
    }
}
