package com.seguros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @class Seguro
 * @brief Representa un seguro contratado por un cliente.
 *
 *        Esta clase es una entidad JPA que mapea la tabla "seguros" en la base
 *        de datos.
 *        Contiene información sobre el nombre, descripción, tipo y precio del
 *        seguro.
 */
@Entity
@Table(name = "seguros")
public class Seguro {

    /**
     * Identificador único del seguro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del seguro.
     */
    @Column(nullable = false, unique = true)
    private String nombre;

    /**
     * Descripción del seguro.
     */
    @Column(nullable = false)
    private String descripcion;

    /**
     * Tipo de seguro.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoSeguro tipoSeguro;

    /**
     * Precio del seguro.
     */
    @Column(nullable = false)
    private Double precio;

    /**
     * Cliente asociado al seguro.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    /**
     * Constructor por defecto.
     */
    public Seguro() {
    }

    /**
     * Constructor completo.
     * 
     * @param nombre      Nombre del seguro.
     * @param descripcion Descripción del seguro.
     * @param tipoSeguro  Tipo de seguro.
     * @param precio      Precio del seguro.
     */
    public Seguro(String nombre, String descripcion, TipoSeguro tipoSeguro, Double precio) {
        this.tipoSeguro = tipoSeguro;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    /**
     * Obtiene el identificador del seguro.
     * 
     * @return id del seguro.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador del seguro.
     * 
     * @param id Identificador a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el tipo de seguro.
     * 
     * @return tipo de seguro.
     */
    public TipoSeguro getTipoSeguro() {
        return tipoSeguro;
    }

    /**
     * Establece el tipo de seguro.
     * 
     * @param tipoSeguro Tipo de seguro a establecer.
     */
    public void setTipoSeguro(TipoSeguro tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    /**
     * Obtiene el nombre del seguro.
     * 
     * @return nombre del seguro.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del seguro.
     * 
     * @param nombre Nombre a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del seguro.
     * 
     * @return descripción del seguro.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del seguro.
     * 
     * @param descripcion Descripción a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio del seguro.
     * 
     * @return precio del seguro.
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del seguro.
     * 
     * @param precio Precio a establecer.
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * Devuelve una representación en cadena del seguro.
     * 
     * @return Cadena con los datos del seguro.
     */
    @Override
    public String toString() {
        return "Seguro{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tipoSeguro=" + tipoSeguro +
                ", precio=" + precio +
                '}';
    }

    /**
     * Compara si dos seguros son iguales según su id.
     * 
     * @param o Objeto a comparar.
     * @return true si los ids son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Seguro seguro = (Seguro) o;
        return id != null && id.equals(seguro.id);
    }

    /**
     * Calcula el hashcode del seguro según su id.
     * 
     * @return hashcode del seguro.
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}