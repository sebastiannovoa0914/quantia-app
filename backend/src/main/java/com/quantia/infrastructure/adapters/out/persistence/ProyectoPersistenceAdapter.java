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
        
        // Mapeo de Dominio a Entidad (Para persistir en DB)
        entity.setIdProyecto(proyecto.getId_proyecto()); // Por si es una actualización
        entity.setNombre(proyecto.getNombre());
        entity.setDescripcion(proyecto.getDescripcion());
        entity.setFechaInicio(proyecto.getFecha_inicio());
        entity.setFechaFin(proyecto.getFecha_fin());
        entity.setIdUsuarioAdmin(proyecto.getId_usuario_admin());
        
        // Mapeo de nuevos campos de Ubicación
        entity.setLatitud(proyecto.getLatitud());
        entity.setLongitud(proyecto.getLongitud());
        
        // Aseguramos que el progreso no sea nulo
        entity.setProgreso(proyecto.getProgreso() != null ? proyecto.getProgreso() : 0);

        ProyectoEntity savedEntity = jpaRepository.save(entity);
        
        // Retornamos el modelo actualizado con el ID generado
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
            p.setProgreso(e.getProgreso() != null ? e.getProgreso() : 0);

            // MAPEO DE UBICACIÓN DESDE LA DB AL DOMINIO
            p.setLatitud(e.getLatitud());
            p.setLongitud(e.getLongitud());

            if (e.getIdUsuarioAdmin() != null) {
                usuarioRepository.findById(e.getIdUsuarioAdmin()).ifPresent(user -> {
                    p.setNombreAdmin(user.getNombre());
                });
            }
            return p;
        }).collect(Collectors.toList());
    }

    public void actualizarProgreso(Long id, Integer nuevoProgreso) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setProgreso(nuevoProgreso);
            jpaRepository.save(entity);
        });
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }
}