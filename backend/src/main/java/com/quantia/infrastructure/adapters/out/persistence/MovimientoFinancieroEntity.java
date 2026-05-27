package com.quantia.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contabilidad")
public class MovimientoFinancieroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
    private Long id;

    @Column(name = "numero_factura", nullable = false)
    private String numeroFactura;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "socio_responsable", nullable = false)
    private String socioResponsable;

    @Column(nullable = false)
    private String tipo; 

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto", referencedColumnName = "id_proyecto", nullable = false)
    private ProyectoEntity proyecto; 

    public MovimientoFinancieroEntity() {}

    // GETTERS Y SETTERS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }
    
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public String getSocioResponsable() { return socioResponsable; }
    public void setSocioResponsable(String socioResponsable) { this.socioResponsable = socioResponsable; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public ProyectoEntity getProyecto() { return proyecto; }
    public void setProyecto(ProyectoEntity proyecto) { this.proyecto = proyecto; }
}