package com.seguros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.seguros.model.Duda;

@Repository
public interface DudaRepository extends JpaRepository<Duda, Long> {

    @Query("SELECT d.asunto FROM Duda d")
    List<String> findAllAsuntos();

}
