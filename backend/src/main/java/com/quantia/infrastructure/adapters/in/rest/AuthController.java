package com.quantia.infrastructure.adapters.in.rest;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.in.UsuarioServicePort;
import com.quantia.infrastructure.config.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthController optimizado para Quantia.
 * Se eliminó @CrossOrigin local para usar la configuración global de SecurityConfig,
 * evitando conflictos de puertos y errores 403 en Docker.
 */
@RestController
@CrossOrigin(origins = "http://localhost", allowCredentials = "true")
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
        // Extraemos campos coincidiendo con los nombres usados en Angular
        String email = credentials.get("email");
        String password = credentials.get("contrasena");

        String token = usuarioService.login(email, password);

        if (token != null) {
            Usuario user = usuarioService.buscarPorEmail(email);
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("nombre", user.getNombre());
            response.put("rol", user.getRol());
            response.put("id", user.getId()); 

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(Map.of("error", "Credenciales inválidas o usuario no encontrado"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        // Aseguramos que el registro devuelva el objeto usuario creado satisfactoriamente
        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }
}