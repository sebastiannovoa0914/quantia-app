package com.quantia.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuarios")
@Data
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    
    private String nombre;
    
    @Column(unique = true)
    private String email;
    
    private String contrasena;
    
    @Enumerated(EnumType.STRING)
    private Rol rol; // El enum que definimos: ADMINISTRADOR, CONTADOR, PROPIETARIO
    
    private boolean activo = true;
}
