package com.quantia.domain.ports.in;

import com.quantia.domain.model.MovimientoFinanciero;
import java.util.List;
import java.util.Map;

public interface MovimientoFinancieroServicePort {
    MovimientoFinanciero registrarMovimiento(MovimientoFinanciero movimiento);
    List<MovimientoFinanciero> listarPorProyecto(Long idProyecto);
    Map<String, Double> obtenerResumenFinanciero(Long idProyecto);
}