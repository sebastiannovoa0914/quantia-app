package com.quantia.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaMovimientoFinancieroRepository extends JpaRepository<MovimientoFinancieroEntity, Long> {
    
    // Filtra los movimientos de un proyecto usando su ID primario interno
    List<MovimientoFinancieroEntity> findByProyectoIdProyectoOrderByFechaDesc(Long idProyecto);
    
    // Trae absolutamente todo el historial contable global de la constructora
    List<MovimientoFinancieroEntity> findAllByOrderByFechaDesc();
}