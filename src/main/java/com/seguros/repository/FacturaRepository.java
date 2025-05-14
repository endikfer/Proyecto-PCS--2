package com.seguros.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


import com.seguros.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByIdCliente(Long idCliente);
    
}
