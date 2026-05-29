package com.quantia.application.services;

import com.quantia.domain.model.MovimientoFinanciero;
import com.quantia.domain.ports.in.MovimientoFinancieroServicePort;
import com.quantia.domain.ports.out.MovimientoFinancieroRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MovimientoFinancieroServiceImpl implements MovimientoFinancieroServicePort {

    private final MovimientoFinancieroRepositoryPort repositoryPort;
    private static final Logger logger = LoggerFactory.getLogger(MovimientoFinancieroServiceImpl.class);

    public MovimientoFinancieroServiceImpl(MovimientoFinancieroRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MovimientoFinanciero registrarMovimiento(MovimientoFinanciero movimiento) {
        logger.info("Registrando nuevo movimiento financiero: tipo={}, valor={}", movimiento.getTipo(), movimiento.getValor());
        try {
            MovimientoFinanciero guardado = repositoryPort.guardar(movimiento);
            logger.info("Movimiento registrado con éxito, ID: {}", guardado.getId());
            return guardado;
        } catch (Exception e) {
            logger.error("Error al registrar movimiento financiero", e);
            throw e;
        }
    }

    @Override
    public List<MovimientoFinanciero> listarPorProyecto(Long idProyecto) {
        logger.debug("Listando movimientos para el proyecto ID: {}", idProyecto);
        return repositoryPort.buscarPorProyecto(idProyecto);
    }

    @Override
    public Map<String, Double> obtenerResumenFinanciero(Long idProyecto) {
        logger.info("Calculando resumen financiero para proyecto ID: {}", idProyecto);
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
        
        logger.debug("Resumen calculado: {}", resumen);
        return resumen;
    }
}