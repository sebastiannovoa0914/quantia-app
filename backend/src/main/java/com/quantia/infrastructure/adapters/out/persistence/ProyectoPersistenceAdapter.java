package com.quantia.infrastructure.adapters.out.persistence;

import com.quantia.domain.model.Proyecto;
import com.quantia.domain.ports.out.ProyectoRepositoryPort;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProyectoPersistenceAdapter implements ProyectoRepositoryPort {

    private final JpaProyectoRepository jpaRepository;
    private final JpaUsuarioRepository usuarioRepository;

    public ProyectoPersistenceAdapter(JpaProyectoRepository jpaRepository, JpaUsuarioRepository usuarioRepository) {
        this.jpaRepository = jpaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Proyecto guardar(Proyecto proyecto) {
        ProyectoEntity entity = new ProyectoEntity();
        
        // Mapeo de Dominios a Entidad
        entity.setNombre(proyecto.getNombre());
        entity.setDescripcion(proyecto.getDescripcion());
        entity.setFechaInicio(proyecto.getFecha_inicio());
        entity.setFechaFin(proyecto.getFecha_fin());
        entity.setIdUsuarioAdmin(proyecto.getId_usuario_admin());
        
        // IMPORTANTE: Aseguramos que el progreso no sea nulo al guardar por primera vez
        entity.setProgreso(proyecto.getProgreso() != null ? proyecto.getProgreso() : 0);

        ProyectoEntity savedEntity = jpaRepository.save(entity);
        
        // Retornamos el modelo con el ID generado por la DB
        proyecto.setId_proyecto(savedEntity.getIdProyecto());
        return proyecto;
    }

    @Override
    public List<Proyecto> listarTodos() {
        List<ProyectoEntity> entidades = jpaRepository.findAll();
        
        return entidades.stream().map(e -> {
            Proyecto p = new Proyecto();
            p.setId_proyecto(e.getIdProyecto());
            p.setNombre(e.getNombre());
            p.setDescripcion(e.getDescripcion());
            p.setFecha_inicio(e.getFechaInicio());
            p.setFecha_fin(e.getFechaFin());
            p.setId_usuario_admin(e.getIdUsuarioAdmin());
            
            // MAPEO DEL PROGRESO: Vital para que Angular lo vea al cargar el panel
            p.setProgreso(e.getProgreso() != null ? e.getProgreso() : 0);

            if (e.getIdUsuarioAdmin() != null) {
                usuarioRepository.findById(e.getIdUsuarioAdmin()).ifPresent(user -> {
                    p.setNombreAdmin(user.getNombre());
                });
            }
            return p;
        }).collect(Collectors.toList());
    }

    /**
     * Actualiza el progreso de forma independiente.
     * Este método será llamado por el controlador REST.
     */
    public void actualizarProgreso(Long id, Integer nuevoProgreso) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setProgreso(nuevoProgreso);
            jpaRepository.save(entity);
        });
    }
    @Override
    public void eliminar(Long id) {
        // Usamos el JpaRepository para borrar por ID
        // deleteById es un método que ya viene incluido en JpaRepository
        jpaRepository.deleteById(id);
    }
}