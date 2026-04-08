package com.quantia.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private String email;
    private String contrasena;
    private String rol; // ADMINISTRADOR, CONTADOR, PROPIETARIO
    private boolean activo;
}