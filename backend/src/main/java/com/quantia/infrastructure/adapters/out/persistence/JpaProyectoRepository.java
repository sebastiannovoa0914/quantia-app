package com.quantia.infrastructure.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProyectoRepository extends JpaRepository<ProyectoEntity, Long> {
}