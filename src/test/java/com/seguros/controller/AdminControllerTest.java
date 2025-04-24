package com.seguros.controller;

import com.seguros.model.Administrador;
import com.seguros.repository.AdministradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_CorrectCredentials_ReturnsSuccess() {
        Administrador inputAdmin = new Administrador();
        inputAdmin.setEmail("admin@gmail.com");
        inputAdmin.setPassword("1234");

        Administrador mockAdmin = new Administrador();
        mockAdmin.setEmail("admin@gmail.com");
        mockAdmin.setPassword("1234");

        when(administradorRepository.findByEmail("admin@gmail.com")).thenReturn(mockAdmin);

        ResponseEntity<?> response = adminController.login(inputAdmin);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Inicio de sesión exitoso", response.getBody());
    }

    @Test
    void login_IncorrectPassword_ReturnsUnauthorized() {
        Administrador inputAdmin = new Administrador();
        inputAdmin.setEmail("admin@gmail.com");
        inputAdmin.setPassword("wrong");

        Administrador mockAdmin = new Administrador();
        mockAdmin.setEmail("admin@gmail.com");
        mockAdmin.setPassword("1234");

        when(administradorRepository.findByEmail("admin@gmail.com")).thenReturn(mockAdmin);

        ResponseEntity<?> response = adminController.login(inputAdmin);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Credenciales incorrectas", response.getBody());
    }

    @Test
    void login_UserNotFound_ReturnsUnauthorized() {
        Administrador inputAdmin = new Administrador();
        inputAdmin.setEmail("admin01@gmail.com");
        inputAdmin.setPassword("1234");

        when(administradorRepository.findByEmail("admin01@gmail.com")).thenReturn(null);

        ResponseEntity<?> response = adminController.login(inputAdmin);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Credenciales incorrectas", response.getBody());
    }

    @Test
    void logout_ReturnsSuccessMessage() {
        ResponseEntity<String> response = adminController.logout();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Sesión cerrada exitosamente.", response.getBody());
    }
}
