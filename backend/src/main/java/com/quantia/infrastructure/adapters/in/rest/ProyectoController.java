package com.quantia.infrastructure.adapters.in.rest;

import com.quantia.domain.model.Proyecto;
import com.quantia.domain.ports.in.ProyectoServicePort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {

    private final ProyectoServicePort proyectoService;

    // Inyectamos el puerto de entrada (Arquitectura Hexagonal)
    public ProyectoController(ProyectoServicePort proyectoService) {
        this.proyectoService = proyectoService;
    }

    /**
     * Endpoint para obtener todas las propiedades/proyectos.
     * Este es el que hace que la interfaz de "Explorar" muestre datos.
     */
    @GetMapping
    public ResponseEntity<List<Proyecto>> listarProyectos() {
        List<Proyecto> proyectos = proyectoService.obtenerTodos();
        return ResponseEntity.ok(proyectos);
    }

    /**
     * Endpoint para guardar un nuevo proyecto.
     */
    @PostMapping
    public ResponseEntity<?> crearProyecto(@RequestBody Proyecto proyecto) {
        // 1. Confirmación técnica en consola
        System.out.println("Proyecto recibido para guardar: " + proyecto.getNombre());
        
        // 2. Ejecución de la lógica de negocio a través del puerto
        Proyecto proyectoGuardado = proyectoService.crearProyecto(proyecto);
        
        // 3. Respuesta estructurada al Frontend
        return ResponseEntity.ok(Map.of(
            "mensaje", "Proyecto guardado exitosamente en la base de datos",
            "id", proyectoGuardado.getId_proyecto()
        ));
    }
}