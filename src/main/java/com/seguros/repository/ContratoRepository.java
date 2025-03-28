package com.seguros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguros.model.Contrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {
    // Método para verificar si un email ya existe
    
}