package com.quantia.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "proyectos") // Debe ser el nombre exacto de tu tabla en MySQL
@Data
public class ProyectoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proyecto")
    private Long idProyecto;

    private String nombre;
    private String descripcion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "id_usuario_admin")
    private Long idUsuarioAdmin;
}