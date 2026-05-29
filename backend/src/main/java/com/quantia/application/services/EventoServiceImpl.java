package com.quantia.application.services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.quantia.domain.model.Evento;
import com.quantia.domain.ports.in.EventoServicePort;
import com.quantia.domain.ports.out.EventoRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EventoServiceImpl implements EventoServicePort {

    private final EventoRepositoryPort repository;
    private static final Logger logger = LoggerFactory.getLogger(EventoServiceImpl.class);

    // Constructor para la inyección de dependencias
    public EventoServiceImpl(EventoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void guardarEvento(Evento evento) {
        // Aquí centralizamos tanto el log como la lógica de persistencia
        logger.info("Intentando guardar evento con ID: {}", evento.getId());
        
        try {
            repository.guardar(evento);
            logger.info("Evento guardado exitosamente con ID: {}", evento.getId());
        } catch (Exception e) {
            logger.error("Error crítico al guardar el evento con ID: {}", evento.getId(), e);
            throw e; // Es importante relanzar la excepción si el negocio lo requiere
        }
    }

    @Override
    public List<Evento> obtenerEventosPorUsuario(Long idUsuario) {
        return repository.obtenerPorUsuario(idUsuario);
    }

    @Override
    public void eliminarEvento(Long id) {
        logger.info("Eliminando evento con ID: {}", id);
        repository.eliminar(id);
    }

    @Override
    public List<Evento> obtenerTodosLosEventos() {
        return repository.findAll();
    }
}