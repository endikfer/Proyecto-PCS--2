package com.seguros.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FacturaTest {

    @Test
    void testConstructorConParametrosYGetters() {
        Long id = 1L;
        String fecha = "2024-05-01";
        float cantidad = 1500.75f;
        String estado = "Pagado";
        Long idCliente = 10L;

        Factura factura = new Factura(id, fecha, cantidad, estado, idCliente);

        assertEquals(id, factura.getId());
        assertEquals(fecha, factura.getFecha());
        assertEquals(cantidad, factura.getCantidad());
        assertEquals(estado, factura.getEstado());
        assertEquals(idCliente, factura.getIdCliente());
    }

    @Test
    void testSetters() {
        Factura factura = new Factura();

        factura.setId(2L);
        factura.setFecha("2024-06-01");
        factura.setCantidad(2000.50f);
        factura.setEstado("Pendiente");
        factura.setIdCliente(20L);

        assertEquals(2L, factura.getId());
        assertEquals("2024-06-01", factura.getFecha());
        assertEquals(2000.50f, factura.getCantidad());
        assertEquals("Pendiente", factura.getEstado());
        assertEquals(20L, factura.getIdCliente());
    }
    
}
