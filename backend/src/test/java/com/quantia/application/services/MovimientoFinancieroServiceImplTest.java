package com.quantia.application.services;

import com.quantia.domain.model.MovimientoFinanciero;
import com.quantia.domain.ports.out.MovimientoFinancieroRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovimientoFinancieroServiceImplTest {

    @Mock
    private MovimientoFinancieroRepositoryPort repositoryPort;

    @InjectMocks
    private MovimientoFinancieroServiceImpl movimientoService;

    @Test
    void obtenerResumenFinanciero_DeberiaCalcularBalanceCorrectamente() {
        // Arrange: Creamos movimientos de prueba
        MovimientoFinanciero ingreso = new MovimientoFinanciero();
        ingreso.setTipo(MovimientoFinanciero.TipoTransaccion.INGRESO);
        ingreso.setValor(new BigDecimal("1000.00"));

        MovimientoFinanciero egreso = new MovimientoFinanciero();
        egreso.setTipo(MovimientoFinanciero.TipoTransaccion.EGRESO);
        egreso.setValor(new BigDecimal("400.00"));

        when(repositoryPort.buscarPorProyecto(1L)).thenReturn(Arrays.asList(ingreso, egreso));

        // Act
        Map<String, Double> resumen = movimientoService.obtenerResumenFinanciero(1L);

        // Assert
        assertEquals(1000.0, resumen.get("ingresos"));
        assertEquals(400.0, resumen.get("egresos"));
        assertEquals(600.0, resumen.get("balance"), "El balance debería ser 600.0");
    }

    @Test
    void registrarMovimiento_DeberiaGuardarCorrectamente() {
        // Arrange
        MovimientoFinanciero m = new MovimientoFinanciero();
        when(repositoryPort.guardar(any())).thenReturn(m);

        // Act
        movimientoService.registrarMovimiento(m);

        // Assert
        verify(repositoryPort, times(1)).guardar(m);
    }
}