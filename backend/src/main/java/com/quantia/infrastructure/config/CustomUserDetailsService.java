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
        UsuarioEntity user = repository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
    
        // Sincronización de Roles para Quantia:
        // NO concatenamos "ROLE_". Usamos el nombre literal de la DB.
        String roleName = user.getRol().toString(); 
        
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(roleName) // Esto pasará "ADMINISTRADOR"
        );
    
        return User.builder()
            .username(user.getEmail())
            .password(user.getContrasena())
            .authorities(authorities)
            .build();
    }
}