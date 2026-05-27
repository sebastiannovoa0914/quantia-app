package com.quantia.infrastructure.adapters.in.rest;

import com.quantia.domain.model.MovimientoFinanciero;
import com.quantia.domain.ports.in.MovimientoFinancieroServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contabilidad")
@CrossOrigin(origins = "http://localhost:4200") // Abre la comunicación directa con tu app Angular
public class MovimientoFinancieroController {

    private final MovimientoFinancieroServicePort servicePort;

    public MovimientoFinancieroController(MovimientoFinancieroServicePort servicePort) {
        this.servicePort = servicePort;
    }

    // Endpoint para guardar un nuevo ingreso/egreso desde la modal de Angular
    @PostMapping("/registrar")
    public ResponseEntity<MovimientoFinanciero> registrarMovimiento(@RequestBody MovimientoFinanciero movimiento) {
        MovimientoFinanciero creado = servicePort.registrarMovimiento(movimiento);
        return ResponseEntity.ok(creado);
    }

    // Endpoint para listar las transacciones (soporta opcionalmente filtro por proyecto)
    @GetMapping("/transacciones")
    public ResponseEntity<List<MovimientoFinanciero>> listarTransacciones(@RequestParam(value = "proyectoId", required = false) Long proyectoId) {
        return ResponseEntity.ok(servicePort.listarPorProyecto(proyectoId));
    }

    // Endpoint para calcular los balances dinámicos de las tarjetas superiores
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Double>> obtenerResumen(@RequestParam(value = "proyectoId", required = false) Long proyectoId) {
        return ResponseEntity.ok(servicePort.obtenerResumenFinanciero(proyectoId));
    }
}