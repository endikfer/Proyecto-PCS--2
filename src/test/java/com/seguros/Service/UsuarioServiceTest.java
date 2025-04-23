package com.seguros.Service;

import com.seguros.repository.AdministradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {

    private AdministradorRepository administradorRepository;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        administradorRepository = mock(AdministradorRepository.class);
        usuarioService = new UsuarioService(administradorRepository);
    }

    @Test
    void testEsAdmin_CuandoExiste() {
        // Arrange
        String email = "admin@example.com";
        when(administradorRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean resultado = usuarioService.esAdmin(email);

        // Assert
        assertTrue(resultado, "Debe retornar true si el email existe como admin");
    }

    @Test
    void testEsAdmin_CuandoNoExiste() {
        // Arrange
        String email = "noadmin@example.com";
        when(administradorRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean resultado = usuarioService.esAdmin(email);

        // Assert
        assertFalse(resultado, "Debe retornar false si el email no existe como admin");
    }
}
