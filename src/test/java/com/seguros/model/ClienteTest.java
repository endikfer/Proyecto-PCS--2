package com.seguros.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("user02", "user02@gmail.com", "user02");
    }

    @Test
    void testConstructorYGetters() {
        assertEquals("user02", cliente.getNombre());
        assertEquals("user02@gmail.com", cliente.getEmail());
        assertEquals("user02", cliente.getPassword());
        assertNull(cliente.getId());
    }

    @Test
    void testSetNombre() {
        cliente.setNombre("user01");
        assertEquals("user01", cliente.getNombre());
    }

    @Test
    void testSetEmail() {
        cliente.setEmail("user01@gmail.com");
        assertEquals("user01@gmail.com", cliente.getEmail());
    }

    @Test
    void testSetPassword() {
        cliente.setPassword("654321");
        assertEquals("654321", cliente.getPassword());
    }

    @Test
    void testValidPasswordLength() {
        cliente.setPassword("abcdef");
        assertTrue(cliente.getPassword().length() >= 6);
    }

    @Test
    void testInvalidPasswordLength() {
        cliente.setPassword("123");
        assertTrue(cliente.getPassword().length() < 6);
    }
}
