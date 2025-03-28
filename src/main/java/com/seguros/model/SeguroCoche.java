package com.seguros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "seguros_coche")
public class SeguroCoche{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int poliza;


    private String matricula;
    private String modelo;
    private String marca;

    @ManyToOne
    @JoinColumn(name = "seguro_id", nullable = false)
    private Seguro seguro;

    // Constructor vacío
    public SeguroCoche() {
        super();
    }

    // Constructor completo
    public SeguroCoche(Seguro seguro, String matricula, String modelo, String marca) {
        this.matricula = matricula;
        this.seguro = seguro;
        this.modelo = modelo;
        this.marca = marca;
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}