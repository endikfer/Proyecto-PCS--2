package com.seguros.controller;

import com.seguros.Service.ClientesPorSeguro;
import com.seguros.Service.SeguroService;
import com.seguros.model.Seguro;
import com.seguros.model.SeguroCasa;
import com.seguros.model.SeguroCoche;
import com.seguros.model.SeguroVida;
import com.seguros.model.TipoSeguro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
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

    // ----------------- TESTS PARA editarSeguro -----------------

    @Test
    public void testEditarSeguro_Exitoso() {
        when(seguroService.editarSeguro(1L, "Seguro A", "Descripción A", "TIPO", 120.0)).thenReturn(true);
        ResponseEntity<String> response = seguroController.editarSeguro(1L, "Seguro A", "Descripción A", "TIPO", 120.0);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testEditarSeguro_IdInvalido() {
        ResponseEntity<String> response = seguroController.editarSeguro(-1L, "Seguro", "Desc", "TIPO", 120.0);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        verify(seguroService, never()).editarSeguro(anyLong(), anyString(), anyString(), anyString(), anyDouble());
    }

    @Test
    public void testEditarSeguro_NombreNulo() {
        ResponseEntity<String> response = seguroController.editarSeguro(1L, null, "Desc", "TIPO", 120.0);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testEditarSeguro_DescripcionVacia() {
        ResponseEntity<String> response = seguroController.editarSeguro(1L, "Nombre", "", "TIPO", 120.0);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testEditarSeguro_DescripcionNula() {
        ResponseEntity<String> response = seguroController.editarSeguro(1L, "Seguro", null, "TIPO", 120.0);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).editarSeguro(any(), any(), any(), any(), any());
    }

    @Test
    public void testEditarSeguro_PrecioInvalido() {
        ResponseEntity<String> response = seguroController.editarSeguro(1L, "Nombre", "Desc", "TIPO", -10.0);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testEditarSeguro_PrecioNulo() {
        ResponseEntity<String> response = seguroController.editarSeguro(1L, "Seguro", "Desc", "TIPO", null);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).editarSeguro(any(), any(), any(), any(), any());
    }

    @Test
    public void testEditarSeguro_SeguroNoEncontrado() {
        when(seguroService.editarSeguro(anyLong(), anyString(), anyString(), anyString(), anyDouble()))
                .thenReturn(false);
        ResponseEntity<String> response = seguroController.editarSeguro(999L, "Seguro", "Desc", "TIPO", 100.0);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Seguro no encontrado.", response.getBody());
    }

    @Test
    public void testEditarSeguro_ConflictoDuplicado() {
        when(seguroService.editarSeguro(anyLong(), anyString(), anyString(), anyString(), anyDouble()))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException("Duplicado"));
        ResponseEntity<String> response = seguroController.editarSeguro(2L, "Seguro", "Desc", "TIPO", 100.0);
        assertEquals(CONFLICT, response.getStatusCode());
        assertEquals("Seguro existente con el mismo nombre. Por favor, elija otro nombre.", response.getBody());
    }

    @Test
    public void testEditarSeguro_ErrorInterno() {
        when(seguroService.editarSeguro(anyLong(), anyString(), anyString(), anyString(), anyDouble()))
                .thenThrow(new RuntimeException("Error inesperado"));
        ResponseEntity<String> response = seguroController.editarSeguro(3L, "Seguro", "Desc", "TIPO", 150.0);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testEditarSeguro_IdNull() {
        ResponseEntity<String> response = seguroController.editarSeguro(null, "Seguro", "Desc", "TIPO", 120.0);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).editarSeguro(any(), any(), any(), any(), any());
    }

    @Test
    public void testEditarSeguro_NombreBlanco() {
        ResponseEntity<String> response = seguroController.editarSeguro(1L, "   ", "Desc", "TIPO", 120.0);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).editarSeguro(any(), any(), any(), any(), any());
    }

    @Test
    public void testEditarSeguro_DescripcionBlanca() {
        ResponseEntity<String> response = seguroController.editarSeguro(1L, "Seguro", "   ", "TIPO", 120.0);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios y el precio debe ser mayor a 0.", response.getBody());
        verify(seguroService, never()).editarSeguro(any(), any(), any(), any(), any());
    }

    // ----------------- TESTS PARA obtenerSeguroPorNombre -----------------

    @Test
    public void testObtenerSeguroPorNombre_Encontrado() {
        Seguro seguro = new Seguro("Auto", "Seguro de auto", TipoSeguro.COCHE, 200.0);
        when(seguroService.obtenerSeguroPorNombre("Auto")).thenReturn(seguro);
        ResponseEntity<Seguro> response = seguroController.obtenerSeguroPorNombre("Auto");
        assertEquals(OK, response.getStatusCode());
        assertEquals(seguro, response.getBody());
    }

    @Test
    public void testObtenerSeguroPorNombre_NoEncontrado() {
        when(seguroService.obtenerSeguroPorNombre("Inexistente")).thenReturn(null);
        ResponseEntity<Seguro> response = seguroController.obtenerSeguroPorNombre("Inexistente");
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ----------------- TESTS PARA eliminarSeguro -----------------

    @Test
    public void testEliminarSeguro_Exitoso() {
        when(seguroService.eliminarSeguro(1L)).thenReturn(true);
        ResponseEntity<String> response = seguroController.eliminarSeguro(1L);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void testEliminarSeguro_IdInvalido() {
        ResponseEntity<String> response = seguroController.eliminarSeguro(0L);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("ID inválido.", response.getBody());
    }

    @Test
    public void testEliminarSeguro_IdNull() {
        ResponseEntity<String> response = seguroController.eliminarSeguro(null);
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("ID inválido.", response.getBody());
    }

    @Test
    public void testEliminarSeguro_SeguroNoEncontrado() {
        when(seguroService.eliminarSeguro(999L)).thenReturn(false);
        ResponseEntity<String> response = seguroController.eliminarSeguro(999L);
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("Seguro no encontrado.", response.getBody());
    }

    @Test
    public void testEliminarSeguro_ErrorInterno() {
        when(seguroService.eliminarSeguro(1L)).thenThrow(new RuntimeException("Error inesperado"));
        ResponseEntity<String> response = seguroController.eliminarSeguro(1L);
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ----------------- TESTS PARA sleccionarSeguro -----------------

    @Test
    void testSeleccionarSeguro_Exitoso() {
        // Simula el comportamiento del servicio
        when(seguroService.seleccionarSeguro(1L, 1L)).thenReturn(true);

        // Llama al método del controlador
        ResponseEntity<String> response = seguroController.seleccionarSeguro(1L, 1L);

        // Verifica el resultado
        assertEquals(OK, response.getStatusCode());
        assertEquals("Seguro seleccionado correctamente", response.getBody());
        verify(seguroService, times(1)).seleccionarSeguro(1L, 1L);
    }

    @Test
    void testSeleccionarSeguro_SeguroIdInvalido() {
        // Llama al método del controlador con seguroId inválido
        ResponseEntity<String> response = seguroController.seleccionarSeguro(-1L, 1L);

        // Verifica el resultado
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Los IDs del seguro y del cliente son obligatorios y deben ser mayores a 0.", response.getBody());
        verify(seguroService, never()).seleccionarSeguro(anyLong(), anyLong());
    }

    @Test
    void testSeleccionarSeguro_ClienteIdInvalido() {
        // Llama al método del controlador con clienteId inválido
        ResponseEntity<String> response = seguroController.seleccionarSeguro(1L, -1L);

        // Verifica el resultado
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals("Los IDs del seguro y del cliente son obligatorios y deben ser mayores a 0.", response.getBody());
        verify(seguroService, never()).seleccionarSeguro(anyLong(), anyLong());
    }

    @Test
    void testSeleccionarSeguro_NoEncontrado() {
        // Simula que el servicio devuelve false, indicando que no se pudo seleccionar
        // el seguro
        when(seguroService.seleccionarSeguro(1L, 1L)).thenReturn(false);

        // Llama al método del controlador
        ResponseEntity<String> response = seguroController.seleccionarSeguro(1L, 1L);

        // Verifica el resultado
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertEquals("No se pudo seleccionar el seguro. Verifique los IDs proporcionados.", response.getBody());
        verify(seguroService, times(1)).seleccionarSeguro(1L, 1L);
    }

    @Test
    void testSeleccionarSeguro_ErrorInterno() {
        // Simula que ocurre una excepción en el servicio
        when(seguroService.seleccionarSeguro(1L, 1L)).thenThrow(new RuntimeException("Error inesperado"));

        // Llama al método del controlador
        ResponseEntity<String> response = seguroController.seleccionarSeguro(1L, 1L);

        // Verifica el resultado
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al seleccionar el seguro: Error inesperado", response.getBody());
        verify(seguroService, times(1)).seleccionarSeguro(1L, 1L);
    }

    @Test
    void testSeleccionarSeguro_SeguroIdNull() {
        ResponseEntity<String> response = seguroController.seleccionarSeguro(null, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Los IDs del seguro y del cliente son obligatorios y deben ser mayores a 0.", response.getBody());
        verify(seguroService, never()).seleccionarSeguro(any(), any());
    }

    @Test
    void testSeleccionarSeguro_ClienteIdNull() {
        ResponseEntity<String> response = seguroController.seleccionarSeguro(1L, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Los IDs del seguro y del cliente son obligatorios y deben ser mayores a 0.", response.getBody());
        verify(seguroService, never()).seleccionarSeguro(any(), any());
    }

    @Test
    void testCantidadClientesPorSeguro() {
        List<ClientesPorSeguro> stats = Arrays.asList(
                new ClientesPorSeguro("Seguro1", 2),
                new ClientesPorSeguro("Seguro2", 0));
        when(seguroService.contarClientesPorSeguro()).thenReturn(stats);

        ResponseEntity<List<ClientesPorSeguro>> response = seguroController.cantidadClientesPorSeguro();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(stats, response.getBody());
        verify(seguroService).contarClientesPorSeguro();
    }

    @Test
    void testGetSegurosPorCliente_ConResultados() {
        Long clienteId = 1L;
        List<Seguro> seguros = Arrays.asList(new Seguro(), new Seguro());
        when(seguroService.obtenerPorCliente(clienteId)).thenReturn(seguros);

        ResponseEntity<List<Seguro>> response = seguroController.getSegurosPorCliente(clienteId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(seguros, response.getBody());
        verify(seguroService).obtenerPorCliente(clienteId);
    }

    @Test
    void testGetSegurosPorCliente_SinResultados() {
        Long clienteId = 2L;
        when(seguroService.obtenerPorCliente(clienteId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Seguro>> response = seguroController.getSegurosPorCliente(clienteId);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(seguroService).obtenerPorCliente(clienteId);
    }

    @Test
    void testGetSegurosPorCliente_Null() {
        Long clienteId = 3L;
        when(seguroService.obtenerPorCliente(clienteId)).thenReturn(null);

        ResponseEntity<List<Seguro>> response = seguroController.getSegurosPorCliente(clienteId);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(seguroService).obtenerPorCliente(clienteId);
    }

    @Test
    void testGuardarSeguroCoche_Exitoso() {
        Seguro seguro = new Seguro("Auto", "Cobertura total", TipoSeguro.COCHE, 1000.0);
        SeguroCoche coche = new SeguroCoche(seguro, "1234ABC", "ModelX", "Tesla");

        when(seguroService.obtenerSeguroPorId(1L)).thenReturn(seguro);
        when(seguroService.guardarSeguroCoche(any(), anyString(), anyString(), anyString())).thenReturn(coche);

        ResponseEntity<?> response = seguroController.guardarSeguroCoche(1L, "1234ABC", "ModelX", "Tesla");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(coche, response.getBody());
    }

    @Test
    void testGuardarSeguroCoche_SeguroNoEncontrado() {
        when(seguroService.obtenerSeguroPorId(99L)).thenReturn(null);

        ResponseEntity<?> response = seguroController.guardarSeguroCoche(99L, "1234ABC", "ModelX", "Tesla");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Seguro no encontrado.", response.getBody());
    }

    @Test
    void testGuardarSeguroCoche_SeguroIdNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroCoche(null, "1234ABC", "ModelX", "Tesla");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCoche_SeguroIdMenorIgualCero() {
        ResponseEntity<?> response = seguroController.guardarSeguroCoche(0L, "1234ABC", "ModelX", "Tesla");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCoche_MatriculaNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroCoche(1L, null, "ModelX", "Tesla");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCoche_MatriculaBlank() {
        ResponseEntity<?> response = seguroController.guardarSeguroCoche(1L, "   ", "ModelX", "Tesla");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCoche_ModeloNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroCoche(1L, "1234ABC", null, "Tesla");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCoche_ModeloBlank() {
        ResponseEntity<?> response = seguroController.guardarSeguroCoche(1L, "1234ABC", "   ", "Tesla");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCoche_MarcaNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroCoche(1L, "1234ABC", "ModelX", null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCoche_MarcaBlank() {
        ResponseEntity<?> response = seguroController.guardarSeguroCoche(1L, "1234ABC", "ModelX", "   ");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCoche_Exception() {
        Seguro seguro = new Seguro("Auto", "Cobertura total", TipoSeguro.COCHE, 1000.0);
        when(seguroService.obtenerSeguroPorId(1L)).thenReturn(seguro);
        when(seguroService.guardarSeguroCoche(any(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("DB error"));

        ResponseEntity<?> response = seguroController.guardarSeguroCoche(1L, "1234ABC", "ModelX", "Tesla");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al guardar el seguro de coche: DB error", response.getBody());
    }

    // ---------------------- VIDA ----------------------

    @Test
    void testGuardarSeguroVida_Exitoso() {
        Seguro seguro = new Seguro("Vida", "Cobertura vida", TipoSeguro.VIDA, 2000.0);
        SeguroVida vida = new SeguroVida(seguro, 35, "Juan, Ana");

        when(seguroService.obtenerSeguroPorId(2L)).thenReturn(seguro);
        when(seguroService.guardarSeguroVida(any(), anyInt(), anyString())).thenReturn(vida);

        ResponseEntity<?> response = seguroController.guardarSeguroVida(2L, 35, "Juan, Ana");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vida, response.getBody());
    }

    @Test
    void testGuardarSeguroVida_SeguroNoEncontrado() {
        when(seguroService.obtenerSeguroPorId(99L)).thenReturn(null);

        ResponseEntity<?> response = seguroController.guardarSeguroVida(99L, 35, "Juan, Ana");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Seguro no encontrado.", response.getBody());
    }

    @Test
    void testGuardarSeguroVida_SeguroIdNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroVida(null, 35, "Juan, Ana");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroVida_SeguroIdMenorIgualCero() {
        ResponseEntity<?> response = seguroController.guardarSeguroVida(0L, 35, "Juan, Ana");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroVida_EdadAseguradoNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroVida(2L, null, "Juan, Ana");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroVida_EdadAseguradoMenorIgualCero() {
        ResponseEntity<?> response = seguroController.guardarSeguroVida(2L, 0, "Juan, Ana");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroVida_BeneficiariosNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroVida(2L, 35, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroVida_BeneficiariosBlank() {
        ResponseEntity<?> response = seguroController.guardarSeguroVida(2L, 35, "   ");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroVida_Exception() {
        Seguro seguro = new Seguro("Vida", "Cobertura vida", TipoSeguro.VIDA, 2000.0);
        when(seguroService.obtenerSeguroPorId(2L)).thenReturn(seguro);
        when(seguroService.guardarSeguroVida(any(), anyInt(), anyString()))
                .thenThrow(new RuntimeException("DB error"));

        ResponseEntity<?> response = seguroController.guardarSeguroVida(2L, 35, "Juan, Ana");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al guardar el seguro de vida: DB error", response.getBody());
    }

    // ---------------------- CASA ----------------------

    @Test
    void testGuardarSeguroCasa_Exitoso() {
        Seguro seguro = new Seguro("Casa", "Cobertura casa", TipoSeguro.CASA, 3000.0);
        SeguroCasa casa = new SeguroCasa(seguro, "Calle Falsa 123", 250000.0, "Piso");

        when(seguroService.obtenerSeguroPorId(3L)).thenReturn(seguro);
        when(seguroService.guardarSeguroCasa(any(), anyString(), anyDouble(), anyString())).thenReturn(casa);

        ResponseEntity<?> response = seguroController.guardarSeguroCasa(3L, "Calle Falsa 123", 250000.0, "Piso");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(casa, response.getBody());
    }

    @Test
    void testGuardarSeguroCasa_SeguroNoEncontrado() {
        when(seguroService.obtenerSeguroPorId(99L)).thenReturn(null);

        ResponseEntity<?> response = seguroController.guardarSeguroCasa(99L, "Calle Falsa 123", 250000.0, "Piso");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Seguro no encontrado.", response.getBody());
    }

    @Test
    void testGuardarSeguroCasa_SeguroIdNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroCasa(null, "Calle Falsa 123", 250000.0, "Piso");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCasa_SeguroIdMenorIgualCero() {
        ResponseEntity<?> response = seguroController.guardarSeguroCasa(0L, "Calle Falsa 123", 250000.0, "Piso");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCasa_DireccionNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroCasa(3L, null, 250000.0, "Piso");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCasa_DireccionBlank() {
        ResponseEntity<?> response = seguroController.guardarSeguroCasa(3L, "   ", 250000.0, "Piso");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCasa_ValorInmuebleNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroCasa(3L, "Calle Falsa 123", null, "Piso");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCasa_ValorInmuebleMenorIgualCero() {
        ResponseEntity<?> response = seguroController.guardarSeguroCasa(3L, "Calle Falsa 123", 0.0, "Piso");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCasa_TipoViviendaNull() {
        ResponseEntity<?> response = seguroController.guardarSeguroCasa(3L, "Calle Falsa 123", 250000.0, null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCasa_TipoViviendaBlank() {
        ResponseEntity<?> response = seguroController.guardarSeguroCasa(3L, "Calle Falsa 123", 250000.0, "   ");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos los campos son obligatorios.", response.getBody());
    }

    @Test
    void testGuardarSeguroCasa_Exception() {
        Seguro seguro = new Seguro("Casa", "Cobertura casa", TipoSeguro.CASA, 3000.0);
        when(seguroService.obtenerSeguroPorId(3L)).thenReturn(seguro);
        when(seguroService.guardarSeguroCasa(any(), anyString(), anyDouble(), anyString()))
                .thenThrow(new RuntimeException("DB error"));

        ResponseEntity<?> response = seguroController.guardarSeguroCasa(3L, "Calle Falsa 123", 250000.0, "Piso");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error al guardar el seguro de casa: DB error", response.getBody());
    }
}