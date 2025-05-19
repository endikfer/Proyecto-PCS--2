package com.seguros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @file Factura.java
 * @brief Entidad que representa una factura en el sistema.
 * 
 * Esta clase mapea la tabla "facturas" de la base de datos y contiene la información
 * relevante de cada factura, como fecha, cantidad, estado e id del cliente asociado.
 * 
 * @author [Tu Nombre]
 * @date 2025-05-19
 */
@Entity
@Table(name = "facturas")
public class Factura {

    /**
     * Identificador único de la factura.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha de emisión de la factura.
     */
    @Column(nullable = false)
    private String fecha;

    /**
     * Cantidad total de la factura.
     */
    @Column(nullable = false)
    private float cantidad;

    /**
     * Estado de la factura (por ejemplo: pagada, pendiente).
     */
    @Column(nullable = false)
    private String estado;

    /**
     * Identificador del cliente asociado a la factura.
     */
    @Column(nullable = false)
    private Long idCliente;

    /**
     * Constructor por defecto.
     */
    public Factura() {
    }

    /**
     * Constructor con parámetros.
     * @param id Identificador de la factura.
     * @param fecha Fecha de emisión.
     * @param cantidad Cantidad total.
     * @param estado Estado de la factura.
     * @param idCliente Identificador del cliente asociado.
     */
    public Factura(Long id, String fecha, float cantidad, String estado, Long idCliente) {
        this.id = id;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.estado = estado;
        this.idCliente = idCliente;
    }

    /**
     * @brief Obtiene el identificador de la factura.
     * @return id de la factura.
     */
    public Long getId() {
        return id;
    }

    /**
     * @brief Establece el identificador de la factura.
     * @param id Identificador de la factura.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @brief Obtiene la fecha de emisión de la factura.
     * @return fecha de la factura.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @brief Establece la fecha de emisión de la factura.
     * @param fecha Fecha de la factura.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @brief Obtiene la cantidad total de la factura.
     * @return cantidad de la factura.
     */
    public float getCantidad() {
        return cantidad;
    }

    /**
     * @brief Establece la cantidad total de la factura.
     * @param cantidad Cantidad de la factura.
     */
    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @brief Obtiene el estado de la factura.
     * @return estado de la factura.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @brief Establece el estado de la factura.
     * @param estado Estado de la factura.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @brief Obtiene el identificador del cliente asociado.
     * @return id del cliente.
     */
    public Long getIdCliente() {
        return idCliente;
    }

    /**
     * @brief Establece el identificador del cliente asociado.
     * @param idCliente Identificador del cliente.
     */
    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }
    
}