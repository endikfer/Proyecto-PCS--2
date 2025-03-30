package com.seguros.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seguros.model.Administrador;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    boolean existsByUsername(String username);
    
    Administrador findByEmail(String email);
}