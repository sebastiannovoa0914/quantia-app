package com.quantia.infrastructure.adapters.in.rest;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.in.UsuarioServicePort;
import com.quantia.infrastructure.config.JwtUtil; // Asegúrate de que esta ruta sea la correcta
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioServicePort usuarioService;
    private final JwtUtil jwtUtil; // <--- Agregamos la herramienta del Token

    // Actualizamos el constructor para que Spring inyecte ambos
    public AuthController(UsuarioServicePort usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        // Extraemos email y contraseña del JSON enviado por Postman
        String email = credentials.get("email");
        String password = credentials.get("contrasena");

        // Llamamos al servicio (el método que vamos a programar a continuación)
        String token = usuarioService.login(email, password);

        if (token != null) {
            // Si el login es exitoso, devolvemos el token en un JSON
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            // Si falla, devolvemos un 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Collections.singletonMap("error", "Credenciales inválidas"));
        }
    }
}