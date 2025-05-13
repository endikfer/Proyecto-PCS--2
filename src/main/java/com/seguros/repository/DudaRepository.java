package com.seguros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seguros.model.Duda;

@Repository
public interface DudaRepository extends JpaRepository<Duda, Long> {

}
