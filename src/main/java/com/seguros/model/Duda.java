package com.seguros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @class Duda
 * @brief Representa una duda enviada por un usuario.
 *
 *        Esta clase es una entidad JPA que mapea la tabla "dudas" en la base de
 *        datos.
 *        Contiene información sobre el asunto y el mensaje de la duda.
 */
@Entity
@Table(name = "dudas")
public class Duda {

    /**
     * Identificador único de la duda.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Asunto de la duda.
     */
    @Column(nullable = false, unique = false)
    private String asunto;

    /**
     * Mensaje o contenido de la duda.
     */
    @Column(nullable = false, unique = false)
    private String mensaje;

    /**
     * Constructor por defecto.
     */
    public Duda() {
    }

    /**
     * Constructor con parámetros.
     * 
     * @param asunto  Asunto de la duda.
     * @param mensaje Mensaje de la duda.
     */
    public Duda(String asunto, String mensaje) {
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el identificador de la duda.
     * 
     * @return id de la duda.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la duda.
     * 
     * @param id Identificador a establecer.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el asunto de la duda.
     * 
     * @return asunto de la duda.
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Establece el asunto de la duda.
     * 
     * @param asunto Asunto a establecer.
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * Obtiene el mensaje de la duda.
     * 
     * @return mensaje de la duda.
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el mensaje de la duda.
     * 
     * @param mensaje Mensaje a establecer.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Devuelve una representación en cadena de la duda.
     * 
     * @return Cadena con los datos de la duda.
     */
    @Override
    public String toString() {
        return "Dudas{" +
                "id=" + id +
                ", asunto='" + asunto + '\'' +
                ", mensaje='" + mensaje + '\'' +
                '}';
    }

    /**
     * Compara si dos dudas son iguales según su id.
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
        Duda duda = (Duda) o;
        return id != null && id.equals(duda.id);
    }

    /**
     * Calcula el hashcode de la duda según su id.
     * 
     * @return hashcode de la duda.
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}