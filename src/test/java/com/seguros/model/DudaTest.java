package com.seguros.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DudaTest {

    @Test
    void testDefaultConstructor() {
        Duda duda = new Duda();
        assertNull(duda.getId());
        assertNull(duda.getAsunto());
        assertNull(duda.getMensaje());
    }

    @Test
    void testParameterizedConstructor() {
        Duda duda = new Duda("asunto1", "mensaje1");
        assertNull(duda.getId());
        assertEquals("asunto1", duda.getAsunto());
        assertEquals("mensaje1", duda.getMensaje());
    }

    @Test
    void testGetSetId() {
        Duda duda = new Duda();
        duda.setId(10L);
        assertEquals(10L, duda.getId());
    }

    @Test
    void testGetSetAsunto() {
        Duda duda = new Duda();
        duda.setAsunto("asunto2");
        assertEquals("asunto2", duda.getAsunto());
    }

    @Test
    void testGetSetMensaje() {
        Duda duda = new Duda();
        duda.setMensaje("mensaje2");
        assertEquals("mensaje2", duda.getMensaje());
    }

    @Test
    void testToString() {
        Duda duda = new Duda("asunto3", "mensaje3");
        duda.setId(5L);
        String str = duda.toString();
        assertTrue(str.contains("id=5"));
        assertTrue(str.contains("asunto='asunto3'"));
        assertTrue(str.contains("mensaje='mensaje3'"));
    }

    @Test
    void testEqualsSameObject() {
        Duda duda = new Duda();
        duda.setId(1L);
        assertEquals(duda, duda);
    }

    @Test
    void testEqualsNull() {
        Duda duda = new Duda();
        duda.setId(1L);
        assertNotEquals(duda, null);
    }

    @Test
    void testEqualsDifferentClass() {
        Duda duda = new Duda();
        duda.setId(1L);
        assertNotEquals(duda, "string");
    }

    @Test
    void testEqualsSameId() {
        Duda duda1 = new Duda();
        duda1.setId(2L);
        Duda duda2 = new Duda();
        duda2.setId(2L);
        assertEquals(duda1, duda2);
    }

    @Test
    void testEqualsDifferentId() {
        Duda duda1 = new Duda();
        duda1.setId(3L);
        Duda duda2 = new Duda();
        duda2.setId(4L);
        assertNotEquals(duda1, duda2);
    }

    @Test
    void testEqualsNullId() {
        Duda duda1 = new Duda();
        Duda duda2 = new Duda();
        assertNotEquals(duda1, duda2);
    }

    @Test
    void testHashCodeWithId() {
        Duda duda1 = new Duda();
        duda1.setId(7L);
        Duda duda2 = new Duda();
        duda2.setId(7L);
        assertEquals(duda1.hashCode(), duda2.hashCode());
    }

    @Test
    void testHashCodeWithNullId() {
        Duda duda = new Duda();
        assertEquals(0, duda.hashCode());
    }
}