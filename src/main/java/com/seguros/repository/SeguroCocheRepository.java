package com.seguros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguros.model.SeguroCoche;

@Repository
public interface SeguroCocheRepository extends JpaRepository<SeguroCoche, Long> {
    
}