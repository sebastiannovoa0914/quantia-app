package com.quantia.domain.ports.in;

import com.quantia.domain.model.Proyecto;
import java.util.List;

public interface ProyectoServicePort {
    Proyecto crearProyecto(Proyecto proyecto);
    List<Proyecto> obtenerTodos();
}