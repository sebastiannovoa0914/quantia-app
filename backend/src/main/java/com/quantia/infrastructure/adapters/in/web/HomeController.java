package com.quantia.infrastructure.adapters.in.web;

import com.quantia.infrastructure.adapters.out.persistence.JpaUsuarioRepository;
import com.quantia.infrastructure.adapters.out.persistence.UsuarioEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final JpaUsuarioRepository usuarioRepository;

    public HomeController(JpaUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil() {
        // 1. Obtener el email desde el Token (SecurityContext)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // 2. Buscar al usuario en la BD para obtener su nombre real
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Obtener el rol limpio
        String rol = auth.getAuthorities().iterator().next().getAuthority();
        
        // 4. Personalizar mensaje según el rol
        String saludoPersonalizado = generarSaludo(usuario.getNombre(), rol);

        return ResponseEntity.ok(Map.of(
            "nombre", usuario.getNombre(),
            "rol", rol,
            "mensaje", saludoPersonalizado,
            "email", email
        ));
    }

    private String generarSaludo(String nombre, String rol) {
        return switch (rol) {
            case "ROLE_ADMINISTRADOR" -> "¡Bienvenido Maestro, " + nombre + "! Tienes acceso total al sistema.";
            case "ROLE_CONTADOR" -> "Hola " + nombre + ", los reportes financieros están listos para revisión.";
            case "ROLE_PROPIETARIO" -> "Bienvenido " + nombre + ", aquí puedes ver el avance de tus proyectos.";
            default -> "Hola " + nombre + ", bienvenido a Quantia.";
        };
    }
}