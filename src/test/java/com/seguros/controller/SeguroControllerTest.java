package com.seguros.controller;

import com.seguros.Service.SeguroService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

class SeguroControllerTest {

    @Mock
    private SeguroService seguroService;

    @InjectMocks
    private SeguroController seguroController;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodosSeguros_ConSeguros() {
        // Simula el comportamiento del servicio
        List<String> segurosMock = Arrays.asList("Seguro1", "Seguro2");
        when(seguroService.obtenerTodosSeguros()).thenReturn(segurosMock);

        // Llama al método del controlador
        ResponseEntity<List<String>> response = seguroController.obtenerTodosSeguros();

        // Verifica el resultado
        assertEquals(OK, response.getStatusCode());
        assertEquals(segurosMock, response.getBody());
        verify(seguroService, times(1)).obtenerTodosSeguros();
    }

    @Test
    void testObtenerTodosSeguros_ConSegurosNulos() {
        // Simula el comportamiento del servicio cuando la lista de seguros es null
        when(seguroService.obtenerTodosSeguros()).thenReturn(null);

        // Llama al método del controlador
        ResponseEntity<List<String>> response = seguroController.obtenerTodosSeguros();

        // Verifica el resultado
        assertEquals(OK, response.getStatusCode());
        assertEquals(Collections.singletonList("vacio"), response.getBody());
        verify(seguroService, times(1)).obtenerTodosSeguros();
    }

    @Test
    void testObtenerTodosSeguros_SinSeguros() {
        // Simula el comportamiento del servicio cuando no hay seguros
        when(seguroService.obtenerTodosSeguros()).thenReturn(Collections.emptyList());

        // Llama al método del controlador
        ResponseEntity<List<String>> response = seguroController.obtenerTodosSeguros();

        // Verifica el resultado
        assertEquals(OK, response.getStatusCode());
        assertEquals(Collections.singletonList("vacio"), response.getBody());
        verify(seguroService, times(1)).obtenerTodosSeguros();
    }

    @Test
    void testObtenerTodosSeguros_ConError() {
        // Simula un error en el servicio
        when(seguroService.obtenerTodosSeguros()).thenThrow(new RuntimeException("Error simulado"));

        // Llama al método del controlador
        ResponseEntity<List<String>> response = seguroController.obtenerTodosSeguros();

        // Verifica el resultado
        assertEquals(500, response.getStatusCodeValue()); // HTTP 500
        verify(seguroService, times(1)).obtenerTodosSeguros();
    }
}