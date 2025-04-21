package com.seguros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguros.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Método para verificar si un email ya existe
    boolean existsByEmail(String email);
    Cliente findByEmail(String email);
}