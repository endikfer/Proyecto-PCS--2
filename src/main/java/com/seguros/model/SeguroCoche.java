package com.seguros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "seguros_coche")
public class SeguroCoche extends Seguro {

    private String poliza;
    private String matricula;
    private String modelo;
    private String marca;

    // Constructor vacío
    public SeguroCoche() {
        super();
    }

    // Constructor completo
    public SeguroCoche(String nombre, String descripcion, TipoSeguro tipoSeguro, Double precio, String poliza, String matricula, String modelo, String marca) {
        super(nombre, descripcion, tipoSeguro, precio);
        this.poliza = poliza;
        this.matricula = matricula;
        this.modelo = modelo;
        this.marca = marca;
    }

    // Getters y Setters
    public String getPoliza() {
        return poliza;
    }

    public void setPoliza(String poliza) {
        this.poliza = poliza;
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