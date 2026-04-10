package com.quantia.domain.ports.out;

import com.quantia.domain.model.Proyecto;
import java.util.List;

public interface ProyectoRepositoryPort {
    Proyecto guardar(Proyecto proyecto);
    List<Proyecto> listarTodos();
}