package com.seguros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;

@Repository
public interface SeguroRepository extends JpaRepository<Seguro, Long> {
    Seguro findByNombre(String nombre);
    List<Seguro> findByTipoSeguro(TipoSeguro tipoSeguro);

}