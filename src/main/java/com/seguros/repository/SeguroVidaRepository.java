package com.seguros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguros.model.SeguroVida;

@Repository
public interface SeguroVidaRepository extends JpaRepository<SeguroVida, Long> {
    
}