package com.quantia.domain.ports.in;

import com.quantia.domain.model.Evento;
import java.util.List;

public interface EventoServicePort {
    void guardarEvento(Evento evento);
    List<Evento> obtenerEventosPorUsuario(Long idUsuario);
    void eliminarEvento(Long id);
    List<Evento> obtenerTodosLosEventos();
}
