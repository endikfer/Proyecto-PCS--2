package com.seguros.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.seguros.repository.DudaRepository;

public class AdminServiceTest {

    @Mock
    private DudaRepository dudaRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        dudaRepository = mock(DudaRepository.class);
        adminService = new AdminService(dudaRepository);
    }

    @Test
    void testGetAllAsuntoDudasReturnsList() {
        List<String> asuntos = Arrays.asList("asunto1", "asunto2");
        when(dudaRepository.findAllAsuntos()).thenReturn(asuntos);

        List<String> result = adminService.getAllAsuntoDudas();

        assertEquals(asuntos, result);
        verify(dudaRepository).findAllAsuntos();
    }

    @Test
    void testGetAllAsuntoDudasReturnsEmptyListWhenNull() {
        when(dudaRepository.findAllAsuntos()).thenReturn(null);

        List<String> result = adminService.getAllAsuntoDudas();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dudaRepository).findAllAsuntos();
    }

    @Test
    void testGetAllAsuntoDudasReturnsEmptyListWhenEmpty() {
        when(dudaRepository.findAllAsuntos()).thenReturn(Collections.emptyList());

        List<String> result = adminService.getAllAsuntoDudas();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(dudaRepository).findAllAsuntos();
    }

    @Test
    void testGetMensajeByAsuntoDudasReturnsMensaje() {
        String asunto = "asunto1";
        String asuntoProcesado = "asunto procesado";
        String mensaje = "mensaje1";

        // Mock static method SeguroService.restaurarEspacios
        try (var mocked = Mockito.mockStatic(SeguroService.class)) {
            mocked.when(() -> SeguroService.restaurarEspacios(asunto)).thenReturn(asuntoProcesado);
            when(dudaRepository.findMensajeByAsunto(asuntoProcesado)).thenReturn(mensaje);

            String result = adminService.getMensajeByAsuntoDudas(asunto);

            assertEquals(mensaje, result);
            verify(dudaRepository).findMensajeByAsunto(asuntoProcesado);
        }
    }

    @Test
    void testGetMensajeByAsuntoDudasReturnsDefaultWhenNull() {
        String asunto = "asunto2";
        String asuntoProcesado = "asunto procesado 2";

        try (var mocked = Mockito.mockStatic(SeguroService.class)) {
            mocked.when(() -> SeguroService.restaurarEspacios(asunto)).thenReturn(asuntoProcesado);
            when(dudaRepository.findMensajeByAsunto(asuntoProcesado)).thenReturn(null);

            String result = adminService.getMensajeByAsuntoDudas(asunto);

            assertEquals("No hay mensaje para este asunto.", result);
            verify(dudaRepository).findMensajeByAsunto(asuntoProcesado);
        }
    }

    @Test
    void testGetMensajeByAsuntoDudasReturnsDefaultWhenEmpty() {
        String asunto = "asunto3";
        String asuntoProcesado = "asunto procesado 3";

        try (var mocked = Mockito.mockStatic(SeguroService.class)) {
            mocked.when(() -> SeguroService.restaurarEspacios(asunto)).thenReturn(asuntoProcesado);
            when(dudaRepository.findMensajeByAsunto(asuntoProcesado)).thenReturn("   ");

            String result = adminService.getMensajeByAsuntoDudas(asunto);

            assertEquals("No hay mensaje para este asunto.", result);
            verify(dudaRepository).findMensajeByAsunto(asuntoProcesado);
        }
    }

}
