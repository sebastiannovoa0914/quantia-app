package com.quantia.domain.ports.out;

import java.util.List;

import com.quantia.domain.model.Evento;

public interface EventoRepositoryPort {
    void guardar(Evento evento);
    List<Evento> obtenerPorUsuario(Long idUsuario);
    void eliminar(Long id);
}