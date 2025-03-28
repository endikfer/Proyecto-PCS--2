package com.seguros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguros.model.Seguro;

@Repository
public interface SeguroRepository extends JpaRepository<Seguro, Long> {
    // Método para verificar si un email ya existe
    
}