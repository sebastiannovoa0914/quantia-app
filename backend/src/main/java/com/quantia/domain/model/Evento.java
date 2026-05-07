package com.quantia.domain.model;

public class Evento {
    private Long id;
    private String titulo;
    private Integer dia;
    private String mesAnio; // Ej: "Mayo 2026"
    private Long idUsuario;
    // Getters y Setters
    public Long getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public Integer getDia() {
        return dia;
    }
    public String getMesAnio() {
        return mesAnio;
    }
    public Long getIdUsuario() {
        return idUsuario;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setDia(Integer dia) {
        this.dia = dia;
    }
    public void setMesAnio(String mesAnio) {
        this.mesAnio = mesAnio;
    }
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}