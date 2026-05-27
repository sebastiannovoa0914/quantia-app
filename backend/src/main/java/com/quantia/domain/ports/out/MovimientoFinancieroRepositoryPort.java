package com.quantia.domain.ports.out;

import com.quantia.domain.model.MovimientoFinanciero;
import java.util.List;

public interface MovimientoFinancieroRepositoryPort {
    MovimientoFinanciero guardar(MovimientoFinanciero movimiento);
    List<MovimientoFinanciero> buscarPorProyecto(Long idProyecto);
    List<MovimientoFinanciero> buscarTodosGlobales();
}