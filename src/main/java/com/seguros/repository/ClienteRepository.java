package com.seguros.repository;

import com.seguros.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Método para verificar si un email ya existe
    boolean existsByEmail(String email);
}