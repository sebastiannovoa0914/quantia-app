package com.quantia.infrastructure.config;

import com.quantia.infrastructure.adapters.out.persistence.UsuarioEntity;
import com.quantia.infrastructure.adapters.out.persistence.JpaUsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final JpaUsuarioRepository repository;

    public CustomUserDetailsService(JpaUsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscamos el usuario en la base de datos quantia_db
        UsuarioEntity user = repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Sincronización de Roles:
        // En la BD aparece como "ADMINISTRADOR".
        // Para que Spring Security y el JWT hagan match, le concatenamos "ROLE_".
        String roleName = user.getRol().toString(); // Funciona para Enum o String
        
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + roleName)
        );

        return User.builder()
            .username(user.getEmail())
            .password(user.getContrasena())
            .authorities(authorities)
            .build();
    }
}