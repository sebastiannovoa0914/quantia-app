package com.quantia.infrastructure.adapters.out.persistence;

import com.quantia.domain.model.MovimientoFinanciero;
import com.quantia.domain.ports.out.MovimientoFinancieroRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovimientoFinancieroPersistenceAdapter implements MovimientoFinancieroRepositoryPort {

    private final JpaMovimientoFinancieroRepository jpaRepository;

    public MovimientoFinancieroPersistenceAdapter(JpaMovimientoFinancieroRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public MovimientoFinanciero guardar(MovimientoFinanciero dominio) {
        MovimientoFinancieroEntity entity = new MovimientoFinancieroEntity();
        entity.setNumeroFactura(dominio.getNumeroFactura());
        entity.setFecha(dominio.getFecha());
        entity.setDescripcion(dominio.getDescripcion());
        entity.setSocioResponsable(dominio.getSocioResponsable());
        entity.setTipo(dominio.getTipo().name());
        entity.setValor(dominio.getValor());

        // Vinculamos el proxy del proyecto usando una entidad vacía con el ID asignado
        ProyectoEntity proyectoEntity = new ProyectoEntity();
        proyectoEntity.setIdProyecto(dominio.getIdProyecto()); 
        entity.setProyecto(proyectoEntity);

        MovimientoFinancieroEntity guardado = jpaRepository.save(entity);
        dominio.setId(guardado.getId());
        return dominio;
    }

    @Override
    public List<MovimientoFinanciero> buscarPorProyecto(Long idProyecto) {
        return jpaRepository.findByProyectoIdProyectoOrderByFechaDesc(idProyecto)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovimientoFinanciero> buscarTodosGlobales() {
        return jpaRepository.findAllByOrderByFechaDesc()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

// Mapeador de Entidad JPA a Modelo de Dominio puro
private MovimientoFinanciero toDomain(MovimientoFinancieroEntity entity) {
    MovimientoFinanciero m = new MovimientoFinanciero();
    m.setId(entity.getId());
    m.setNumeroFactura(entity.getNumeroFactura());
    m.setFecha(entity.getFecha());
    m.setString(entity.getDescripcion()); // Usa el método setter que creaste en tu dominio
    m.setSocioResponsable(entity.getSocioResponsable());
    m.setTipo(MovimientoFinanciero.TipoTransaccion.valueOf(entity.getTipo()));
    m.setValor(entity.getValor());
    m.setIdProyecto(entity.getProyecto().getIdProyecto());
    return m;
}
}