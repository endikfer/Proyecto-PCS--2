package com.seguros.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ContratoTest {
    @Test
    void constructorVacioDebeCrearContrato() {
        Contrato contrato = new Contrato();
        assertNotNull(contrato);
    }

    @Test
    void constructorCompletoDebeInicializarCampos() {
        Cliente cliente = new Cliente("Juan", "juan@gmail.com", "123456");
        Seguro seguro = new Seguro("Seguro Vida", "Cobertura total", TipoSeguro.VIDA, 100.0);
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 1);

        Contrato contrato = new Contrato(1L, cliente, seguro, inicio, fin);

        assertEquals(1L, contrato.getId());
        assertEquals(cliente, contrato.getCliente());
        assertEquals(seguro, contrato.getSeguro());
        assertEquals(inicio, contrato.getFechaInicio());
        assertEquals(fin, contrato.getFechaFin());
    }

    @Test
    void settersYGettersFuncionanCorrectamente() {
        Contrato contrato = new Contrato();

        Cliente cliente = new Cliente("Ana", "ana@gmail.com", "password123");
        Seguro seguro = new Seguro("Seguro Hogar", "Protección completa", TipoSeguro.CASA, 150.0);
        LocalDate inicio = LocalDate.of(2023, 5, 10);
        LocalDate fin = LocalDate.of(2024, 5, 10);

        contrato.setId(2L);
        contrato.setCliente(cliente);
        contrato.setSeguro(seguro);
        contrato.setFechaInicio(inicio);
        contrato.setFechaFin(fin);

        assertEquals(2L, contrato.getId());
        assertEquals(cliente, contrato.getCliente());
        assertEquals(seguro, contrato.getSeguro());
        assertEquals(inicio, contrato.getFechaInicio());
        assertEquals(fin, contrato.getFechaFin());
    }
}
