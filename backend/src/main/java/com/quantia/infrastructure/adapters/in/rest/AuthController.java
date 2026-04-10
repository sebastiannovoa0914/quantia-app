package com.quantia.infrastructure.adapters.in.rest;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.in.UsuarioServicePort;
import com.quantia.infrastructure.config.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioServicePort usuarioService;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioServicePort usuarioService, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("contrasena");

        // 1. Intentamos el login
        String token = usuarioService.login(email, password);

        if (token != null) {
            // 2. Buscamos el usuario
            Usuario user = usuarioService.buscarPorEmail(email);
            
            // 3. Preparamos la respuesta para Angular
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("nombre", user.getNombre());
            response.put("rol", user.getRol());
            
            // CLAVE: Usamos .getId() porque así se llama en tu clase Usuario.java
            // Esto enviará el ID real a Angular y ya no verás el 0 en los proyectos.
            response.put("id", user.getId()); 

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error", "Credenciales inválidas"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuario));
    }
}