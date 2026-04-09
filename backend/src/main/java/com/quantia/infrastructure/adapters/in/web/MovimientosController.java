package com.quantia.infrastructure.adapters.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/movimientos")
public class MovimientosController {

    @GetMapping
    public ResponseEntity<?> listarMovimientos() {
        // Por ahora, devolvemos un mensaje de prueba para confirmar que funciona
        return ResponseEntity.ok(List.of(
            Map.of("id", 1, "descripcion", "Prueba de conexión", "monto", 5000)
        ));
    }
}