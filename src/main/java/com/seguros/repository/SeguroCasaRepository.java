package com.seguros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguros.model.SeguroCasa;

@Repository
public interface SeguroCasaRepository extends JpaRepository<SeguroCasa, Long> {
    
}