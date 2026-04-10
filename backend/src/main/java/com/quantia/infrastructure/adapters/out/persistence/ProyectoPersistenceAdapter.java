package com.quantia.infrastructure.adapters.out.persistence;

import com.quantia.domain.model.Proyecto;
import com.quantia.domain.ports.out.ProyectoRepositoryPort;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProyectoPersistenceAdapter implements ProyectoRepositoryPort {

    private final JpaProyectoRepository jpaRepository;
    private final JpaUsuarioRepository usuarioRepository; // Inyectado correctamente

    public ProyectoPersistenceAdapter(JpaProyectoRepository jpaRepository, JpaUsuarioRepository usuarioRepository) {
        this.jpaRepository = jpaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Proyecto guardar(Proyecto proyecto) {
        ProyectoEntity entity = new ProyectoEntity();
        
        entity.setNombre(proyecto.getNombre());
        entity.setDescripcion(proyecto.getDescripcion());
        entity.setFechaInicio(proyecto.getFecha_inicio());
        entity.setFechaFin(proyecto.getFecha_fin());
        entity.setIdUsuarioAdmin(proyecto.getId_usuario_admin());

        ProyectoEntity savedEntity = jpaRepository.save(entity);
        
        // Sincronizamos el ID generado por MySQL de vuelta al modelo de dominio
        proyecto.setId_proyecto(savedEntity.getIdProyecto());
        
        return proyecto;
    }

    @Override
    public List<Proyecto> listarTodos() {
        return jpaRepository.findAll().stream().map(e -> {
            Proyecto p = new Proyecto();
            p.setId_proyecto(e.getIdProyecto());
            p.setNombre(e.getNombre());
            p.setDescripcion(e.getDescripcion());
            p.setFecha_inicio(e.getFechaInicio());
            p.setFecha_fin(e.getFechaFin());
            p.setId_usuario_admin(e.getIdUsuarioAdmin());

            // --- LÓGICA CORREGIDA: Obtener el nombre del Administrador ---
            // Buscamos al usuario por su ID y asignamos el nombre al objeto Proyecto
            if (e.getIdUsuarioAdmin() != null) {
                usuarioRepository.findById(e.getIdUsuarioAdmin()).ifPresent(user -> {
                    p.setNombreAdmin(user.getNombre());
                });
            }

            return p;
        }).collect(Collectors.toList());
    }
}