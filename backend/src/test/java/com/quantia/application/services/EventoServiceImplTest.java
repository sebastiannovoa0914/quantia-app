package com.quantia.application.services;

import com.quantia.domain.model.Evento;
import com.quantia.domain.ports.out.EventoRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventoServiceImplTest {

    @Mock
    private EventoRepositoryPort repository;

    @InjectMocks
    private EventoServiceImpl eventoService;

    @Test
    void guardarEvento_DeberiaLlamarAlRepositorio() {
        // Arrange
        Evento evento = new Evento();
        evento.setId(1L);

        // Act
        eventoService.guardarEvento(evento);

        // Assert
        verify(repository, times(1)).guardar(evento);
    }

    @Test
    void obtenerEventosPorUsuario_DeberiaRetornarLista() {
        // Arrange
        Long userId = 10L;
        List<Evento> mockEventos = Arrays.asList(new Evento(), new Evento());
        when(repository.obtenerPorUsuario(userId)).thenReturn(mockEventos);

        // Act
        List<Evento> result = eventoService.obtenerEventosPorUsuario(userId);

        // Assert
        assertEquals(2, result.size());
        verify(repository, times(1)).obtenerPorUsuario(userId);
    }

    @Test
    void eliminarEvento_DeberiaLlamarAlRepositorio() {
        // Act
        eventoService.eliminarEvento(1L);

        // Assert
        verify(repository, times(1)).eliminar(1L);
    }
}