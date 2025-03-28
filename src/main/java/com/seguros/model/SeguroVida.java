package com.seguros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "seguros_vida")
public class SeguroVida extends Seguro {

    private Integer edadAsegurado;
    private String beneficiarios;

    // Constructor vacío
    public SeguroVida() {
        super();
    }

    // Constructor completo
    public SeguroVida(String nombre, String descripcion, TipoSeguro tipoSeguro, Double precio, Integer edadAsegurado, Double montoAsegurado, String beneficiarios) {
        super(nombre, descripcion, tipoSeguro, precio);
        this.edadAsegurado = edadAsegurado;
        this.beneficiarios = beneficiarios;
    }

    // Getters y Setters
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