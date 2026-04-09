package com.quantia.infrastructure.adapters.in.rest;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.in.UsuarioServicePort;
import com.quantia.infrastructure.config.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

        // 1. Obtenemos el token del servicio
        String token = usuarioService.login(email, password);

        if (token != null) {
            // 2. Buscamos el usuario completo para obtener su nombre y rol reales
            Usuario user = usuarioService.buscarPorEmail(email);
            
            // 3. Creamos la respuesta con las llaves que el Frontend espera
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("nombre", user.getNombre()); // Viene del @Data de tu modelo
            response.put("rol", user.getRol());       // Viene del @Data de tu modelo

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