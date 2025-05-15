package com.seguros.controller;

import com.seguros.Service.AdminService;
import com.seguros.model.Administrador;
import com.seguros.repository.AdministradorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdminControllerTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    void setUp() {
        adminService = mock(AdminService.class);
        administradorRepository = mock(AdministradorRepository.class);
        adminController = new AdminController(adminService, administradorRepository);
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

    @Test
    void testObtenerTodosAsuntosOk() {
        List<String> asuntos = Arrays.asList("a", "b");
        when(adminService.getAllAsuntoDudas()).thenReturn(asuntos);

        ResponseEntity<?> response = adminController.obtenerTodosAsuntos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(asuntos, response.getBody());
    }

    @Test
    void testObtenerTodosAsuntosNoContentWhenEmpty() {
        when(adminService.getAllAsuntoDudas()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = adminController.obtenerTodosAsuntos();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("No se encontraron asuntos.", response.getBody());
    }

    @Test
    void testObtenerTodosAsuntosNoContentWhenNull() {
        when(adminService.getAllAsuntoDudas()).thenReturn(null);

        ResponseEntity<?> response = adminController.obtenerTodosAsuntos();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("No se encontraron asuntos.", response.getBody());
    }

    @Test
    void testObtenerTodosAsuntosInternalServerError() {
        when(adminService.getAllAsuntoDudas()).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<?> response = adminController.obtenerTodosAsuntos();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testObtenerMensajeByAsuntoOk() {
        String asunto = "asunto";
        String mensaje = "mensaje";
        when(adminService.getMensajeByAsuntoDudas(asunto)).thenReturn(mensaje);

        ResponseEntity<String> response = adminController.obtenerMensajeByAsunto(asunto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mensaje, response.getBody());
    }

    @Test
    void testObtenerMensajeByAsuntoNoContentWhenNull() {
        String asunto = "asunto";
        when(adminService.getMensajeByAsuntoDudas(asunto)).thenReturn(null);

        ResponseEntity<String> response = adminController.obtenerMensajeByAsunto(asunto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("No se encontró un mensaje para el asunto proporcionado.", response.getBody());
    }

    @Test
    void testObtenerMensajeByAsuntoNoContentWhenEmpty() {
        String asunto = "asunto";
        when(adminService.getMensajeByAsuntoDudas(asunto)).thenReturn("   ");

        ResponseEntity<String> response = adminController.obtenerMensajeByAsunto(asunto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("No se encontró un mensaje para el asunto proporcionado.", response.getBody());
    }

    @Test
    void testObtenerMensajeByAsuntoInternalServerError() {
        String asunto = "asunto";
        when(adminService.getMensajeByAsuntoDudas(asunto)).thenThrow(new RuntimeException("DB error"));

        ResponseEntity<String> response = adminController.obtenerMensajeByAsunto(asunto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
