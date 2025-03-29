package com.seguros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "seguros_coche")
public class SeguroCoche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int poliza;

    @Column(nullable = false)
    private String matricula;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
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

    @Override
    public String toString() {
        return "SeguroCoche{" +
                "poliza=" + poliza +
                ", matricula='" + matricula + '\'' +
                ", modelo='" + modelo + '\'' +
                ", marca='" + marca + '\'' +
                ", seguro=" + (seguro != null ? seguro.getId() : null) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SeguroCoche that = (SeguroCoche) o;
        return poliza == that.poliza;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(poliza);
    }
}