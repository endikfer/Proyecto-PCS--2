package com.seguros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "seguros_casa")
public class SeguroCasa extends Seguro {

    private String direccion;
    private Double valorInmueble;
    private String tipoVivienda;

    // Constructor vacío
    public SeguroCasa() {
        super();
    }

    // Constructor completo
    public SeguroCasa(String nombre, String descripcion, TipoSeguro tipoSeguro, Double precio, String direccion, Double valorInmueble, String tipoVivienda) {
        super(nombre, descripcion, tipoSeguro, precio);
        this.direccion = direccion;
        this.valorInmueble = valorInmueble;
        this.tipoVivienda = tipoVivienda;
    }

    // Getters y Setters
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