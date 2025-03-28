package com.seguros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "seguros_casa")
public class SeguroCasa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int poliza;

    private String direccion;

    private Double valorInmueble;

    private String tipoVivienda;

    @ManyToOne
    @JoinColumn(name = "seguro_id", nullable = false)
    private Seguro seguro;

    // Constructor vacío
    public SeguroCasa() {
        super();
    }

    // Constructor completo
    public SeguroCasa(Seguro seguro, String direccion, Double valorInmueble, String tipoVivienda) {
        this.direccion = direccion;
        this.seguro = seguro;
        this.valorInmueble = valorInmueble;
        this.tipoVivienda = tipoVivienda;
    }

    // Getters y Setters
    public int getPoliza() {
        return poliza;
    }

    public void setPoliza(int poliza) {
        this.poliza = poliza;
    }

    public Seguro getSeguro() {
        return seguro;
    }

    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getValorInmueble() {
        return valorInmueble;
    }

    public void setValorInmueble(Double valorInmueble) {
        this.valorInmueble = valorInmueble;
    }

    public String getTipoVivienda() {
        return tipoVivienda;
    }

    public void setTipoVivienda(String tipoVivienda) {
        this.tipoVivienda = tipoVivienda;
    }
}