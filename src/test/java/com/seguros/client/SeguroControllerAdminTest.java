package com.seguros.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.seguros.model.Administrador;

public class SeguroControllerAdminTest {
    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> responseMock;

    private SeguroControllerAdmin adminController;

    @BeforeEach
    void setUp() {
        // Crear un Administrador válido
        MockitoAnnotations.openMocks(this);
        adminController = new SeguroControllerAdmin("localhost", "8080");
    }

    /*
     * @Test
     * void testCrearSeguroCodigo200() throws Exception {
     * when(responseMock.statusCode()).thenReturn(200);
     * when(responseMock.body()).thenReturn("");
     * when(httpClientMock.send(any(HttpRequest.class),
     * any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);
     * 
     * assertDoesNotThrow(() -> adminController.crearSeguro("s", "s", "VIDA", 1.0));
     * assertEquals(200, responseMock.statusCode());
     * 
     * verify(httpClientMock).send(any(HttpRequest.class),
     * any(HttpResponse.BodyHandler.class));
     * }
     */

    /*
     * @Test
     * void testCrearSeguroCodigo400() throws Exception {
     * when(responseMock.statusCode()).thenReturn(400);
     * when(responseMock.body()).thenReturn("Datos inválidos");
     * when(httpClientMock.send(any(HttpRequest.class),
     * any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);
     * 
     * IllegalArgumentException exception =
     * assertThrows(IllegalArgumentException.class,
     * () -> adminController.crearSeguro("Seguro1", "Cobertura", "casa", 500.0));
     * assertTrue(exception.getMessage().
     * contains("Error al crear el seguro: Datos inválidos"));
     * 
     * verify(httpClientMock).send(any(HttpRequest.class),
     * any(HttpResponse.BodyHandler.class));
     * }
     * 
     * @Test
     * void testCrearSeguroCodigo500() throws Exception {
     * when(responseMock.statusCode()).thenReturn(500);
     * when(httpClientMock.send(any(HttpRequest.class),
     * any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);
     * 
     * RuntimeException exception = assertThrows(RuntimeException.class,
     * () -> adminController.crearSeguro("Seguro2", "Cobertura", "COCHE", 2000.0));
     * assertEquals("Error interno del servidor.", exception.getMessage());
     * 
     * verify(httpClientMock).send(any(HttpRequest.class),
     * any(HttpResponse.BodyHandler.class));
     * }
     * 
     * @Test
     * void testCrearSeguroCodigoDesconocido() throws Exception {
     * when(responseMock.statusCode()).thenReturn(418);
     * when(httpClientMock.send(any(HttpRequest.class),
     * any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);
     * 
     * RuntimeException exception = assertThrows(RuntimeException.class,
     * () -> adminController.crearSeguro("Seguro3", "Cobertura", "CASA", 1500.0));
     * assertTrue(exception.getMessage().
     * contains("Fallo al crear el seguro con el código de estado: 418"));
     * 
     * verify(httpClientMock).send(any(HttpRequest.class),
     * any(HttpResponse.BodyHandler.class));
     * }
     */

    @Test
    void testCrearSeguroIOException() throws Exception {
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Error de conexión"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> adminController.crearSeguro("Seguro4", "Cobertura", "VIDA", 300.0));
        assertTrue(exception.getMessage().contains("Error creando el seguro."));
    }

    @Test
    void testCrearSeguroInterruptedException() throws Exception {
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("Operación interrumpida"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> adminController.crearSeguro("Seguro5", "Cobertura", "COCHE", 700.0));
        assertTrue(exception.getMessage().contains("Error creando el seguro."));
    }
}
