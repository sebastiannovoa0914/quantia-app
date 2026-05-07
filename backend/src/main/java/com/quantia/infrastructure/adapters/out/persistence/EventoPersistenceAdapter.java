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
        // Creamos la entidad (Infraestructura) a partir del modelo (Dominio)
        EventoEntity entity = new EventoEntity();
        entity.setTitulo(ev.getTitulo());
        entity.setDia(ev.getDia());
        entity.setMesAnio(ev.getMesAnio());
        entity.setIdUsuario(ev.getIdUsuario());
        
        repository.save(entity);
    }

    @Override
    public List<Evento> obtenerPorUsuario(Long idUsuario) {
        // Convertimos la lista de entidades de MySQL a objetos de dominio
        return repository.findByIdUsuario(idUsuario).stream().map(e -> {
            Evento ev = new Evento();
            ev.setId(e.getId());
            ev.setTitulo(e.getTitulo());
            ev.setDia(e.getDia());
            ev.setMesAnio(e.getMesAnio());
            ev.setIdUsuario(e.getIdUsuario()); // No olvides este para mantener el vínculo
            return ev;
        }).collect(Collectors.toList());
    }
    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}