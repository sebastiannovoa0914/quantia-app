package com.quantia.application.services;

import com.quantia.domain.model.Evento;
import com.quantia.domain.ports.in.EventoServicePort;
import com.quantia.domain.ports.out.EventoRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service // <-- Esta anotación es la que soluciona el error en el log
public class EventoServiceImpl implements EventoServicePort {

    private final EventoRepositoryPort repository;

    // Inyectamos el puerto de salida (el adaptador de persistencia)
    public EventoServiceImpl(EventoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void guardarEvento(Evento evento) {
        repository.guardar(evento);
    }

    @Override
    public List<Evento> obtenerEventosPorUsuario(Long idUsuario) {
        return repository.obtenerPorUsuario(idUsuario);
    }

    @Override
    public void eliminarEvento(Long id) {
        repository.eliminar(id);
    }
}