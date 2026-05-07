package com.quantia.infrastructure.adapters.out.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

// IMPORTACIONES CRÍTICAS (Asegúrate de que estas rutas sean exactas)
import com.quantia.domain.model.Evento;
import com.quantia.domain.ports.out.EventoRepositoryPort;

@Component
public class EventoPersistenceAdapter implements EventoRepositoryPort {

    private final JpaEventoRepository repository;

    public EventoPersistenceAdapter(JpaEventoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void guardar(Evento ev) {
        EventoEntity entity = new EventoEntity();
        entity.setTitulo(ev.getTitulo());
        entity.setDia(ev.getDia());
        entity.setMesAnio(ev.getMesAnio());
        entity.setIdUsuario(ev.getIdUsuario());
        
        repository.save(entity);
    }

    @Override
    public List<Evento> obtenerPorUsuario(Long idUsuario) {
        return repository.findByIdUsuario(idUsuario).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    // --- NUEVO MÉTODO PARA LA AGENDA COMPARTIDA ---
    @Override
    public List<Evento> findAll() {
        // Trae todos los registros de la DB y los convierte al modelo de Dominio
        return repository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    /**
     * Método auxiliar para no repetir código de conversión
     */
    private Evento mapToDomain(EventoEntity entity) {
        Evento ev = new Evento();
        ev.setId(entity.getId());
        ev.setTitulo(entity.getTitulo());
        ev.setDia(entity.getDia());
        ev.setMesAnio(entity.getMesAnio());
        ev.setIdUsuario(entity.getIdUsuario());
        return ev;
    }
}