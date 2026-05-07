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

    public ProyectoController(ProyectoServicePort proyectoService) {
        this.proyectoService = proyectoService;
    }

    @GetMapping
    public ResponseEntity<List<Proyecto>> listarProyectos() {
        List<Proyecto> proyectos = proyectoService.obtenerTodos();
        return ResponseEntity.ok(proyectos);
    }

    @PostMapping
    public ResponseEntity<?> crearProyecto(@RequestBody Proyecto proyecto) {
        System.out.println("Proyecto recibido para guardar: " + proyecto.getNombre());
        Proyecto proyectoGuardado = proyectoService.crearProyecto(proyecto);
        
        return ResponseEntity.ok(Map.of(
            "mensaje", "Proyecto guardado exitosamente en la base de datos",
            "id", proyectoGuardado.getId_proyecto()
        ));
    }

    /**
     * Endpoint para actualizar el progreso de la barra.
     */
    @PatchMapping("/{id}/progreso")
    public ResponseEntity<?> actualizarProgreso(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> body) {
        
        try {
            if (body.containsKey("progreso")) {
                Integer nuevoProgreso = (Integer) body.get("progreso");
                proyectoService.actualizarProgreso(id, nuevoProgreso);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().body("El campo 'progreso' es obligatorio");
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar progreso: " + e.getMessage());
        }
    }

    /**
     * NUEVO: Endpoint para eliminar un proyecto por su ID.
     * Este método es el que llamará el botón de eliminar desde Angular.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProyecto(@PathVariable Long id) {
        try {
            // Llamamos al puerto de entrada del servicio
            proyectoService.eliminarProyecto(id);
            return ResponseEntity.ok(Map.of("mensaje", "Proyecto eliminado exitosamente"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Error al eliminar el proyecto: " + e.getMessage()));
        }
    }
}