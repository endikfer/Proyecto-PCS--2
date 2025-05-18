package com.seguros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.seguros.model.Duda;

/**
 * @interface DudaRepository
 * @brief Repositorio JPA para la entidad Duda.
 *
 *        Proporciona métodos para acceder y consultar dudas en la base de
 *        datos.
 */
@Repository
public interface DudaRepository extends JpaRepository<Duda, Long> {

    /**
     * Obtiene una lista con todos los asuntos de las dudas.
     * 
     * @return Lista de asuntos.
     */
    @Query("SELECT d.asunto FROM Duda d")
    List<String> findAllAsuntos();

    /**
     * Busca el mensaje de una duda dado su asunto.
     * 
     * @param asunto Asunto de la duda.
     * @return Mensaje correspondiente al asunto.
     */
    @Query("SELECT d.mensaje FROM Duda d WHERE d.asunto = :asunto")
    String findMensajeByAsunto(String asunto);

}