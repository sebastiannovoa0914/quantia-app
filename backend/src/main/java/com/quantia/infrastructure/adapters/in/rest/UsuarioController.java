package com.quantia.infrastructure.adapters.in.rest;

import com.quantia.domain.model.Usuario;
import com.quantia.domain.ports.in.UsuarioServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/funciones")
public class UsuarioController {

    private final UsuarioServicePort usuarioService;

    public UsuarioController(UsuarioServicePort usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/propietario")
    public ResponseEntity<Usuario> crearPropietario(@RequestBody Usuario usuario) {
        // Asignamos los valores obligatorios de tu tabla
        usuario.setRol("PROPIETARIO");
        usuario.setActivo(true);
        
        Usuario nuevoPropietario = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(nuevoPropietario);
    }
}