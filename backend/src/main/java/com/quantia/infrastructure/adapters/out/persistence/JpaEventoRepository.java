package com.quantia.infrastructure.adapters.out.persistence;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JpaEventoRepository extends JpaRepository<EventoEntity, Long> {
    
    // Spring genera automáticamente la consulta: 
    // SELECT * FROM agenda_eventos WHERE id_usuario = ?
    List<EventoEntity> findByIdUsuario(Long idUsuario);
}