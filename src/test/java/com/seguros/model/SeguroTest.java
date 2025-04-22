package com.seguros.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SeguroTest {

    @Test
    void testConstructorYGetters() {
        TipoSeguro tipoSeguro = TipoSeguro.VIDA;
        Seguro seguro = new Seguro("Seguro Vida", "Cobertura completa", tipoSeguro, 1000.0);

        assertEquals("Seguro Vida", seguro.getNombre());
        assertEquals("Cobertura completa", seguro.getDescripcion());
        assertEquals(tipoSeguro, seguro.getTipoSeguro());
        assertEquals(1000.0, seguro.getPrecio());
    }

    @Test
    void testSetters() {
        Seguro seguro = new Seguro();
        seguro.setNombre("Seguro Auto");
        seguro.setDescripcion("Protección contra accidentes");
        seguro.setTipoSeguro(TipoSeguro.COCHE);
        seguro.setPrecio(500.0);
        seguro.setId(1L);

        assertEquals("Seguro Auto", seguro.getNombre());
        assertEquals("Protección contra accidentes", seguro.getDescripcion());
        assertEquals(TipoSeguro.COCHE, seguro.getTipoSeguro());
        assertEquals(500.0, seguro.getPrecio());
        assertEquals(1L, seguro.getId());
    }

    @Test
    @SuppressWarnings("ObjectEqualsNull")
    void testEquals() {
        Seguro seguro1 = new Seguro();
        seguro1.setId(1L);
        Seguro seguro2 = new Seguro();
        seguro2.setId(1L);
        Seguro seguro3 = new Seguro();
        seguro3.setId(2L);
        Seguro seguroSinId = new Seguro();

        assertTrue(seguro1.equals(seguro1));

        assertFalse(seguro1.equals(null));

        assertFalse(seguro1.equals("No soy un seguro"));

        assertTrue(seguro1.equals(seguro2));

        assertFalse(seguro1.equals(seguro3));

        assertFalse(seguro1.equals(seguroSinId));
        assertFalse(seguroSinId.equals(seguro1));
    }

    @Test
    void testHashCode() {
        Seguro seguro1 = new Seguro();
        seguro1.setId(1L);
        Seguro seguro2 = new Seguro();
        seguro2.setId(1L);
        Seguro seguro3 = new Seguro();
        seguro3.setId(2L);
        Seguro seguroSinId = new Seguro();

        assertEquals(seguro1.hashCode(), seguro2.hashCode());
        assertNotEquals(seguro1.hashCode(), seguro3.hashCode());
        assertEquals(0, seguroSinId.hashCode());
    }

    @Test
    void testToString() {
        Seguro seguro = new Seguro("Seguro Hogar", "Protección contra incendios", TipoSeguro.CASA, 1200.0);
        seguro.setId(3L);

        String expectedString = "Seguro{id=3, nombre='Seguro Hogar', descripcion='Protección contra incendios', tipoSeguro=CASA, precio=1200.0}";
        assertEquals(expectedString, seguro.toString());
    }
}
