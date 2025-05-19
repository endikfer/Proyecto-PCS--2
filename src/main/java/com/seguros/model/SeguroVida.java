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
@Table(name = "seguros_vida")
public class SeguroVida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int poliza;

    @Column(nullable = false)
    private Integer edadAsegurado;

    @Column(nullable = false)
    private String beneficiarios;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "seguro_id", nullable = false)
    private Seguro seguro;
    

    // Constructor vacío
    public SeguroVida() {
        super();
    }

    // Constructor completo
    public SeguroVida(Seguro seguro, Cliente cliente, Integer edadAsegurado, String beneficiarios) {
        this.seguro = seguro;
        this.cliente = cliente;
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

    // Getter y Setter para cliente
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "SeguroVida{" +
                "poliza=" + poliza +
                ", edadAsegurado=" + edadAsegurado +
                ", beneficiarios='" + beneficiarios + '\'' +
                ", seguro=" + (seguro != null ? seguro.getId() : null) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SeguroVida that = (SeguroVida) o;
        return poliza == that.poliza;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(poliza);
    }
}