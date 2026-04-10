package com.quantia.domain.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProyectoResponse {
    private Long id_proyecto;
    private String nombre;
    private String descripcion;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private String nombreAdmin; // Aquí guardaremos el nombre del usuario 11
}