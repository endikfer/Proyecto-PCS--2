package com.seguros.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ClientesPorSeguroTest {
    @Test
    void testConstructorYGetters() {
        // Arrange
        String nombreSeguro = "Seguro de Vida";
        long cantidadClientes = 150L;

        // Act
        ClientesPorSeguro cps = new ClientesPorSeguro(nombreSeguro, cantidadClientes);

        // Assert
        assertEquals(nombreSeguro, cps.getNombreSeguro());
        assertEquals(cantidadClientes, cps.getCantidadClientes());
    }

    @Test
    void testSetters() {
        // Arrange
        ClientesPorSeguro cps = new ClientesPorSeguro("Inicial", 0L);

        // Act
        cps.setNombreSeguro("Seguro de Hogar");
        cps.setCantidadClientes(75L);

        // Assert
        assertEquals("Seguro de Hogar", cps.getNombreSeguro());
        assertEquals(75L, cps.getCantidadClientes());
    }
}


