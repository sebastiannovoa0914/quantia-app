package com.quantia.application.services;

import com.quantia.domain.model.Proyecto;
import com.quantia.domain.ports.in.ProyectoServicePort;
import com.quantia.domain.ports.out.ProyectoRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProyectoServiceImpl implements ProyectoServicePort {

    private final ProyectoRepositoryPort proyectoRepository;

    public ProyectoServiceImpl(ProyectoRepositoryPort proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    @Override
    public Proyecto crearProyecto(Proyecto proyecto) {
        return proyectoRepository.guardar(proyecto);
    }

    @Override
    public void actualizarProgreso(Long id, Integer progreso) {
        proyectoRepository.actualizarProgreso(id, progreso);
    }

    @Override
    public List<Proyecto> obtenerTodos() {
        return proyectoRepository.listarTodos();
    }

    // AÑADE ESTO PARA QUITAR EL ERROR
    @Override
    public void eliminarProyecto(Long id) {
        // Le pedimos al puerto de salida (repositorio) que haga el trabajo sucio
        proyectoRepository.eliminar(id);
    }
}