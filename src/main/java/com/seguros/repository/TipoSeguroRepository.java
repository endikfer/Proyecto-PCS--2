package com.seguros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguros.model.TipoSeguro;

@Repository
public interface TipoSeguroRepository extends JpaRepository<TipoSeguro, Long> {
    // Método para verificar si un email ya existe
    
}