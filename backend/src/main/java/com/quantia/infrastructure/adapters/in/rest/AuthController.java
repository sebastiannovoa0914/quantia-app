package com.quantia.infrastructure.adapters.in.rest;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.in.UsuarioServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioServicePort usuarioService;

    public AuthController(UsuarioServicePort usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.registrarUsuario(usuario));
    }
}
