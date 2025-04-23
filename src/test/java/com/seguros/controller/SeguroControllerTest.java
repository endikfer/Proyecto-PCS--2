package com.seguros.controller;

import com.seguros.Service.SeguroService;
import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    void testObtenerTodosSeguros_SegurosDisponibles() {
        // Mock del servicio
        Seguro seguroVida = new Seguro("VIDA", "Seguro de vida", TipoSeguro.VIDA, 120.50);
        Seguro seguroCasa = new Seguro("CASA", "Seguro de casa", TipoSeguro.CASA, 200.75);
        List<Seguro> mockSeguros = List.of(seguroVida, seguroCasa);
        when(seguroService.obtenerTodosSeguros()).thenReturn(mockSeguros);

        // Prueba
        ResponseEntity<List<Seguro>> response = seguroController.obtenerTodosSeguros();

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockSeguros, response.getBody());
        verify(seguroService, times(1)).obtenerTodosSeguros();
    }

    @Test
    void testObtenerTodosSeguros_SegurosNull() {
        // Mock del servicio para devolver null
        when(seguroService.obtenerTodosSeguros()).thenReturn(null);

        // Prueba
        ResponseEntity<List<Seguro>> response = seguroController.obtenerTodosSeguros();

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        Seguro seguroVacio = response.getBody().get(0);
        assertEquals("vacio", seguroVacio.getNombre());
        assertNull(seguroVacio.getDescripcion());
        assertNull(seguroVacio.getTipoSeguro());
        assertNull(seguroVacio.getPrecio());
        verify(seguroService, times(1)).obtenerTodosSeguros();
    }

    @Test
    void testObtenerTodosSeguros_SegurosVacios() {
        // Mock del servicio para devolver una lista vacía
        when(seguroService.obtenerTodosSeguros()).thenReturn(Collections.emptyList());

        // Prueba
        ResponseEntity<List<Seguro>> response = seguroController.obtenerTodosSeguros();

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        Seguro seguroVacio = response.getBody().get(0);
        assertEquals("vacio", seguroVacio.getNombre());
        assertNull(seguroVacio.getDescripcion());
        assertNull(seguroVacio.getTipoSeguro());
        assertNull(seguroVacio.getPrecio());
        verify(seguroService, times(1)).obtenerTodosSeguros();
    }

    @Test
    void testObtenerTodosSeguros_ErrorDelSistema() {
        // Mock del servicio para lanzar una excepción
        when(seguroService.obtenerTodosSeguros()).thenThrow(new RuntimeException("Error simulado"));

        // Prueba
        ResponseEntity<List<Seguro>> response = seguroController.obtenerTodosSeguros();

        // Verificaciones
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(seguroService, times(1)).obtenerTodosSeguros();
    }

    @Test
    void testObtenerSegurosPorTipo_SegurosDisponibles() {
        // Mock del servicio
        Seguro seguroVida = new Seguro("VIDA", "Seguro de vida", TipoSeguro.VIDA, 120.50);
        List<Seguro> mockSeguros = List.of(seguroVida);
        when(seguroService.obtenerSegurosPorTipo("VIDA")).thenReturn(mockSeguros);

        // Prueba
        ResponseEntity<List<Seguro>> response = seguroController.obtenerSegurosPorTipo("VIDA");

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockSeguros, response.getBody());
        verify(seguroService, times(1)).obtenerSegurosPorTipo("VIDA");
    }

    @Test
    void testObtenerSegurosPorTipo_SegurosNull() {
        // Mock del servicio para devolver null
        when(seguroService.obtenerSegurosPorTipo("VIDA")).thenReturn(null);

        // Prueba
        ResponseEntity<List<Seguro>> response = seguroController.obtenerSegurosPorTipo("VIDA");

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        Seguro seguroVacio = response.getBody().get(0);
        assertEquals("vacio", seguroVacio.getNombre());
        assertNull(seguroVacio.getDescripcion());
        assertNull(seguroVacio.getTipoSeguro());
        assertNull(seguroVacio.getPrecio());
        verify(seguroService, times(1)).obtenerSegurosPorTipo("VIDA");
    }

    @Test
    void testObtenerSegurosPorTipo_SegurosVacios() {
        // Mock del servicio para devolver una lista vacía
        when(seguroService.obtenerSegurosPorTipo("VIDA")).thenReturn(Collections.emptyList());

        // Prueba
        ResponseEntity<List<Seguro>> response = seguroController.obtenerSegurosPorTipo("VIDA");

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        Seguro seguroVacio = response.getBody().get(0);
        assertEquals("vacio", seguroVacio.getNombre());
        assertNull(seguroVacio.getDescripcion());
        assertNull(seguroVacio.getTipoSeguro());
        assertNull(seguroVacio.getPrecio());
        verify(seguroService, times(1)).obtenerSegurosPorTipo("VIDA");
    }

    @Test
    void testObtenerSegurosPorTipo_ErrorDelSistema() {
        // Mock del servicio para lanzar una excepción
        when(seguroService.obtenerSegurosPorTipo("VIDA")).thenThrow(new RuntimeException("Error simulado"));

        // Prueba
        ResponseEntity<List<Seguro>> response = seguroController.obtenerSegurosPorTipo("VIDA");

        // Verificaciones
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(seguroService, times(1)).obtenerSegurosPorTipo("VIDA");
    }
}