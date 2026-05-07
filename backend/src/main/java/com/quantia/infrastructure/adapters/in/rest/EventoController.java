package com.quantia.infrastructure.adapters.in.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quantia.domain.model.Evento;
import com.quantia.domain.ports.in.EventoServicePort;

@RestController
@RequestMapping("/api/agenda")
public class EventoController {
    private final EventoServicePort service;

    public EventoController(EventoServicePort service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Evento evento) {
        service.guardarEvento(evento);
        return ResponseEntity.ok(Map.of("mensaje", "Evento guardado"));
    }

    @GetMapping
    public ResponseEntity<List<Evento>> listarTodo() {
        // Aquí el service debería llamar a un findAll() general
        return ResponseEntity.ok(service.obtenerTodosLosEventos()); 
    }

    // --- AÑADE ESTE MÉTODO ---
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        service.eliminarEvento(id);
        return ResponseEntity.ok(Map.of("mensaje", "Evento eliminado con éxito"));
    }
}
