package com.quantia.infrastructure.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "agenda_eventos")
public class EventoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private Integer dia;

    @Column(name = "mes_anio", nullable = false)
    private String mesAnio;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    // --- CONSTRUCTORES ---
    public EventoEntity() {}

    public EventoEntity(Long id, String titulo, Integer dia, String mesAnio, Long idUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.dia = dia;
        this.mesAnio = mesAnio;
        this.idUsuario = idUsuario;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public String getMesAnio() {
        return mesAnio;
    }

    public void setMesAnio(String mesAnio) {
        this.mesAnio = mesAnio;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}