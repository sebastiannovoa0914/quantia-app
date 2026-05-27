package com.quantia.application.services;

import com.quantia.domain.model.MovimientoFinanciero;
import com.quantia.domain.ports.in.MovimientoFinancieroServicePort;
import com.quantia.domain.ports.out.MovimientoFinancieroRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovimientoFinancieroServiceImpl implements MovimientoFinancieroServicePort {

    private final MovimientoFinancieroRepositoryPort repositoryPort;

    // Inyección mediante constructor, respetando los principios de la arquitectura
    public MovimientoFinancieroServiceImpl(MovimientoFinancieroRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MovimientoFinanciero registrarMovimiento(MovimientoFinanciero movimiento) {
        // Aquí puedes meter validaciones de negocio en el futuro si lo necesitas
        return repositoryPort.guardar(movimiento);
    }

    @Override
    public List<MovimientoFinanciero> listarPorProyecto(Long idProyecto) {
        if (idProyecto == null || idProyecto == 0) {
            return repositoryPort.buscarTodosGlobales();
        }
        return repositoryPort.buscarPorProyecto(idProyecto);
    }

    @Override
    public Map<String, Double> obtenerResumenFinanciero(Long idProyecto) {
        List<MovimientoFinanciero> movimientos = this.listarPorProyecto(idProyecto);

        double ingresos = 0.0;
        double egresos = 0.0;

        for (MovimientoFinanciero m : movimientos) {
            if (m.getTipo() == MovimientoFinanciero.TipoTransaccion.INGRESO) {
                ingresos += m.getValor().doubleValue();
            } else if (m.getTipo() == MovimientoFinanciero.TipoTransaccion.EGRESO) {
                egresos += m.getValor().doubleValue();
            }
        }

        Map<String, Double> resumen = new HashMap<>();
        resumen.put("ingresos", ingresos);
        resumen.put("egresos", egresos);
        resumen.put("balance", ingresos - egresos);

        return resumen;
    }
}