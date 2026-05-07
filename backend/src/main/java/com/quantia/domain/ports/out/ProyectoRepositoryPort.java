package com.quantia.domain.ports.out;

import com.quantia.domain.model.Proyecto;
import java.util.List;

public interface ProyectoRepositoryPort {
    Proyecto guardar(Proyecto proyecto);
    List<Proyecto> listarTodos();
    void actualizarProgreso(Long id, Integer progreso);
    
    // NUEVO: Definición para persistencia
    void eliminar(Long id); 
}