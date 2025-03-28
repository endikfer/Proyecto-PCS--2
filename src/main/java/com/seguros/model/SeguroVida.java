package com.seguros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "seguros_vida")
public class SeguroVida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int poliza;

    @ManyToOne
    @JoinColumn(name = "seguro_id", nullable = false)
    private Seguro seguro;

    private Integer edadAsegurado;
    
    private String beneficiarios;

    // Constructor vacío
    public SeguroVida() {
        super();
    }

    // Constructor completo
    public SeguroVida(Seguro seguro, Integer edadAsegurado, String beneficiarios) {
        this.seguro = seguro;
        this.edadAsegurado = edadAsegurado;
        this.beneficiarios = beneficiarios;
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

    public Integer getEdadAsegurado() {
        return edadAsegurado;
    }

    public void setEdadAsegurado(Integer edadAsegurado) {
        this.edadAsegurado = edadAsegurado;
    }

    public String getBeneficiarios() {
        return beneficiarios;
    }

    public void setBeneficiarios(String beneficiarios) {
        this.beneficiarios = beneficiarios;
    }
}