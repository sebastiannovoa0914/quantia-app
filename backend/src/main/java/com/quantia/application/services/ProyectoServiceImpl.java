package com.quantia.application.services;

import com.quantia.domain.model.Proyecto;
import com.quantia.domain.ports.in.ProyectoServicePort;
import com.quantia.domain.ports.out.ProyectoRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProyectoServiceImpl implements ProyectoServicePort {

    private final ProyectoRepositoryPort proyectoRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProyectoServiceImpl.class);

    public ProyectoServiceImpl(ProyectoRepositoryPort proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Proyecto crearProyecto(Proyecto proyecto) {
        logger.info("Creando nuevo proyecto: {}", proyecto.getNombre());
        try {
            Proyecto guardado = proyectoRepository.guardar(proyecto);
            logger.info("Proyecto creado exitosamente con ID: {}", guardado.getId());
            return guardado;
        } catch (Exception e) {
            logger.error("Error al crear el proyecto: {}", proyecto.getNombre(), e);
            throw e;
        }
    }

    @Override
    public void actualizarProgreso(Long id, Integer progreso) {
        logger.info("Actualizando progreso del proyecto ID: {} a {}%", id, progreso);
        try {
            proyectoRepository.actualizarProgreso(id, progreso);
            logger.info("Progreso actualizado con éxito para ID: {}", id);
        } catch (Exception e) {
            logger.error("Error al actualizar progreso del proyecto ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        logger.debug("Consultando lista completa de proyectos");
        return proyectoRepository.listarTodos();
    }

    @Override
    public void eliminarProyecto(Long id) {
        logger.info("Eliminando proyecto con ID: {}", id);
        try {
            proyectoRepository.eliminar(id);
            logger.info("Proyecto ID: {} eliminado correctamente", id);
        } catch (Exception e) {
            logger.error("Error al eliminar el proyecto ID: {}", id, e);
            throw e;
        }
    }
}