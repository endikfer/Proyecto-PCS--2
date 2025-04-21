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
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

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
    void testCrearSeguro_Exitoso() {
        // Simula el comportamiento del servicio
        doNothing().when(seguroService).crearSeguro("Seguro1", "Descripción1", "Tipo1", 100.0);

        // Llama al método del controlador
        ResponseEntity<String> response = seguroController.crearSeguro("Seguro1", "Descripción1", "Tipo1", 100.0);

        // Verifica el resultado
        assertEquals(OK, response.getStatusCode());
        verify(seguroService, times(1)).crearSeguro("Seguro1", "Descripción1", "Tipo1", 100.0);
    }

    @Test
    void testCrearSeguro_NombreNulo() {
        // Llama al método del controlador con nombre nulo
        ResponseEntity<String> response = seguroController.crearSeguro(null, "Descripción1", "Tipo1", 100.0);

        // Verifica el resultado
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).crearSeguro(anyString(), anyString(), anyString(), anyDouble());
    }

    @Test
    void testCrearSeguro_NombreVacio() {
        // Llama al método del controlador con nombre vacío
        ResponseEntity<String> response = seguroController.crearSeguro("", "Descripción1", "Tipo1", 100.0);

        // Verifica el resultado
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).crearSeguro(anyString(), anyString(), anyString(), anyDouble());
    }

    @Test
    void testCrearSeguro_DescripcionNula() {
        // Llama al método del controlador con descripción nula
        ResponseEntity<String> response = seguroController.crearSeguro("Seguro1", null, "Tipo1", 100.0);

        // Verifica el resultado
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).crearSeguro(anyString(), anyString(), anyString(), anyDouble());
    }

    @Test
    void testCrearSeguro_DescripcionVacia() {
        // Llama al método del controlador con descripción vacía
        ResponseEntity<String> response = seguroController.crearSeguro("Seguro1", "", "Tipo1", 100.0);

        // Verifica el resultado
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).crearSeguro(anyString(), anyString(), anyString(), anyDouble());
    }

    @Test
    void testCrearSeguro_PrecioNulo() {
        // Llama al método del controlador con precio nulo
        ResponseEntity<String> response = seguroController.crearSeguro("Seguro1", "Descripción1", "Tipo1", null);

        // Verifica el resultado
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).crearSeguro(anyString(), anyString(), anyString(), anyDouble());
    }

    @Test
    void testCrearSeguro_PrecioMenorOIgualACero() {
        // Llama al método del controlador con precio menor o igual a 0
        ResponseEntity<String> response = seguroController.crearSeguro("Seguro1", "Descripción1", "Tipo1", 0.0);

        // Verifica el resultado
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).crearSeguro(anyString(), anyString(), anyString(), anyDouble());
    }

    @Test
    void testCrearSeguro_NombreDuplicado() {
        // Simula una excepción de violación de unicidad
        doThrow(new org.springframework.dao.DataIntegrityViolationException("Duplicado"))
                .when(seguroService).crearSeguro("Seguro1", "Descripción1", "Tipo1", 100.0);

        // Llama al método del controlador
        ResponseEntity<String> response = seguroController.crearSeguro("Seguro1", "Descripción1", "Tipo1", 100.0);

        // Verifica el resultado
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("El nombre del seguro ya existe. Por favor, elija otro nombre.", response.getBody());
        verify(seguroService, times(1)).crearSeguro("Seguro1", "Descripción1", "Tipo1", 100.0);
    }

    @Test
    void testCrearSeguro_ErrorInterno() {
        // Simula una excepción genérica
        doThrow(new RuntimeException("Error interno"))
                .when(seguroService).crearSeguro("Seguro1", "Descripción1", "Tipo1", 100.0);

        // Llama al método del controlador
        ResponseEntity<String> response = seguroController.crearSeguro("Seguro1", "Descripción1", "Tipo1", 100.0);

        // Verifica el resultado
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(seguroService, times(1)).crearSeguro("Seguro1", "Descripción1", "Tipo1", 100.0);
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