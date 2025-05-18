package com.seguros.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.seguros.model.Factura;
import com.seguros.repository.FacturaRepository;
import com.seguros.repository.SeguroRepository;

public class FacturaServiceTest {

    private FacturaRepository facturaRepository;
    private FacturaService facturaService;
    private SeguroService seguroService;
    

    @BeforeEach
    void setUp() {
        facturaRepository = mock(FacturaRepository.class);
        facturaService = new FacturaService(facturaRepository, seguroService);
        // Usamos reflexión para inyectar el mock porque no hay constructor
        try {
            java.lang.reflect.Field field = FacturaService.class.getDeclaredField("facturaRepository");
            field.setAccessible(true);
            field.set(facturaService, facturaRepository);
        } catch (Exception e) {
            fail("Error al inyectar dependencia: " + e.getMessage());
        }
    }

    @Test
    void testGetFacturasByClienteId_CuandoExistenFacturas() {
        // Arrange
        Long clienteId = 1L;
        List<Factura> facturas = Arrays.asList(new Factura(), new Factura());
        when(facturaRepository.findByIdCliente(clienteId)).thenReturn(facturas);

        // Act
        List<Factura> resultado = facturaService.getFacturasByClienteId(clienteId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debe retornar la cantidad correcta de facturas");
    }

    @Test
    void testGetFacturasByClienteId_CuandoNoExistenFacturas() {
        // Arrange
        Long clienteId = 2L;
        when(facturaRepository.findByIdCliente(clienteId)).thenReturn(List.of());

        // Act
        List<Factura> resultado = facturaService.getFacturasByClienteId(clienteId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "Debe retornar una lista vacía si no hay facturas");
    }
    
}
