package com.quantia.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimientoFinanciero {
    private Long id;
    private String numeroFactura;
    private LocalDate fecha;
    private String descripcion;
    private String socioResponsable;
    private TipoTransaccion tipo;
    private BigDecimal valor;
    private Long idProyecto; // Solo necesitamos el ID del proyecto vinculante

    public enum TipoTransaccion {
        INGRESO, EGRESO
    }

    // --- CONSTRUCTORES ---
    public MovimientoFinanciero() {}

    public MovimientoFinanciero(Long id, String numeroFactura, LocalDate fecha, String descripcion, 
                               String socioResponsable, TipoTransaccion tipo, BigDecimal valor, Long idProyecto) {
        this.id = id;
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.socioResponsable = socioResponsable;
        this.tipo = tipo;
        this.valor = valor;
        this.idProyecto = idProyecto;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getDescripcion() { return descripcion; }
    public void setString(String descripcion) { this.descripcion = descripcion; }
    public String getSocioResponsable() { return socioResponsable; }
    public void setSocioResponsable(String socioResponsable) { this.socioResponsable = socioResponsable; }
    public TipoTransaccion getTipo() { return tipo; }
    public void setTipo(TipoTransaccion tipo) { this.tipo = tipo; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public Long getIdProyecto() { return idProyecto; }
    public void setIdProyecto(Long idProyecto) { this.idProyecto = idProyecto; }
}