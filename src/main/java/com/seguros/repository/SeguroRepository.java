package com.seguros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;

/**
 * @interface SeguroRepository
 * @brief Repositorio JPA para la entidad Seguro.
 *
 *        Proporciona métodos para acceder y consultar seguros en la base de
 *        datos.
 */
@Repository
public interface SeguroRepository extends JpaRepository<Seguro, Long> {

    /**
     * Busca un seguro por su nombre.
     * 
     * @param nombre Nombre del seguro.
     * @return Seguro correspondiente al nombre.
     */
    Seguro findByNombre(String nombre);

    /**
     * Busca todos los seguros de un tipo específico.
     * 
     * @param tipoSeguro Tipo de seguro.
     * @return Lista de seguros del tipo especificado.
     */
    List<Seguro> findByTipoSeguro(TipoSeguro tipoSeguro);

    /**
     * Busca todos los seguros asociados a un cliente por su ID.
     * 
     * @param clienteID ID del cliente.
     * @return Lista de seguros asociados al cliente.
     */
    List<Seguro> findByClienteId(Long clienteID);
}