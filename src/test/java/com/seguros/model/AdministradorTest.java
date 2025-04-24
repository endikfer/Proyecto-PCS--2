package com.seguros.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdministradorTest {
    @Test
    void constructorSinArgumentosDebeCrearObjeto() {
        Administrador admin = new Administrador();
        assertNotNull(admin);
    }

    @Test
    void constructorConArgumentosDebeInicializarCampos() {
        Administrador admin = new Administrador("Admin", "admin@gmail.com", "1234");

        assertEquals("Admin", admin.getNombre());
        assertEquals("admin@gmail.com", admin.getEmail());
        assertEquals("1234", admin.getPassword());
    }

    @Test
    void settersYGettersFuncionanCorrectamente() {
        Administrador admin = new Administrador();
        admin.setNombre("Laura");
        admin.setEmail("laura@gmail.com");
        admin.setPassword("secreta");

        assertEquals("Laura", admin.getNombre());
        assertEquals("laura@gmail.com", admin.getEmail());
        assertEquals("secreta", admin.getPassword());
    }

    @Test
    void idDebeSerNullAlCrearAdministrador() {
        Administrador admin = new Administrador("Carlos", "carlos@gmail.com", "123456");
        assertNull(admin.getId(), "El ID debe ser null antes de ser persistido por JPA");
    }
}

