package com.seguros.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.seguros.model.Seguro;

public class SeguroControllerClientTest {
    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> responseMock;

    private SeguroControllerClient clientController;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientController = new SeguroControllerClient(httpClientMock, "localhost", "8080");
    }

    // @Test
    // void testConstructorOriginal() {
    // // Crear una instancia del controlador usando el constructor original
    // SeguroControllerClient client = new SeguroControllerClient("localhost",
    // "8080");

    // // Verificar que la URL base se construyó correctamente
    // String expectedBaseUrl = "http://localhost:8080";
    // assertEquals(expectedBaseUrl, client.getBaseUrl());

    // // Verificar que el HttpClient no sea nulo
    // assertNotNull(client.getHttpClient());
    // }

    @Test
    void testObtenerTodosSegurosCodigo200() throws Exception {
        // Configurar el mock para devolver un código 200 y un cuerpo válido
        String jsonResponse = "[{\"id\":1,\"nombre\":\"Seguro1\"},{\"id\":2,\"nombre\":\"Seguro2\"}]";
        when(responseMock.statusCode()).thenReturn(200);
        when(responseMock.body()).thenReturn(jsonResponse);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);

        // Ejecutar el método
        List<Seguro> seguros = clientController.obtenerTodosSeguros();

        // Verificar el resultado
        assertEquals(2, seguros.size());
        assertEquals("Seguro1", seguros.get(0).getNombre());
        assertEquals("Seguro2", seguros.get(1).getNombre());

        // Verificar que el mock fue llamado
        verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void testObtenerTodosSegurosCodigoError() throws Exception {
        // Configurar el mock para devolver un código de error
        when(responseMock.statusCode()).thenReturn(500);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);

        // Ejecutar el método y verificar la excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientController.obtenerTodosSeguros());
        assertEquals("Error al obtener seguros: 500", exception.getMessage());

        // Verificar que el mock fue llamado
        verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void testObtenerTodosSegurosIOException() throws Exception {
        // Configurar el mock para lanzar una IOException
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Error de conexión"));

        // Ejecutar el método y verificar la excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientController.obtenerTodosSeguros());
        assertTrue(exception.getMessage().contains("Error obteniendo seguros."));

        // Verificar que el mock fue llamado
        verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void testObtenerTodosSegurosInterruptedException() throws Exception {
        // Configurar el mock para lanzar una InterruptedException
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("Operación interrumpida"));

        // Ejecutar el método y verificar la excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientController.obtenerTodosSeguros());
        assertTrue(exception.getMessage().contains("Error obteniendo seguros."));

        // Verificar que el mock fue llamado
        verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void testObtenerSegurosPorTipoCodigo200() throws Exception {
        // Configurar el mock para devolver un código 200 y un cuerpo válido
        String jsonResponse = "[{\"id\":1,\"nombre\":\"Seguro1\"},{\"id\":2,\"nombre\":\"Seguro2\"}]";
        when(responseMock.statusCode()).thenReturn(200);
        when(responseMock.body()).thenReturn(jsonResponse);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);

        // Ejecutar el método
        List<Seguro> seguros = clientController.obtenerSegurosPorTipo("VIDA");

        // Verificar el resultado
        assertEquals(2, seguros.size());
        assertEquals("Seguro1", seguros.get(0).getNombre());
        assertEquals("Seguro2", seguros.get(1).getNombre());

        // Verificar que el mock fue llamado
        verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void testObtenerSegurosPorTipoCodigoError() throws Exception {
        // Configurar el mock para devolver un código de error
        when(responseMock.statusCode()).thenReturn(500);
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);

        // Ejecutar el método y verificar la excepción
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> clientController.obtenerSegurosPorTipo("VIDA"));
        assertEquals("Error al obtener seguros por tipo: 500", exception.getMessage());

        // Verificar que el mock fue llamado
        verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void testObtenerSegurosPorTipoIOException() throws Exception {
        // Configurar el mock para lanzar una IOException
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Error de conexión"));

        // Ejecutar el método y verificar la excepción
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> clientController.obtenerSegurosPorTipo("VIDA"));
        assertTrue(exception.getMessage().contains("Error obteniendo seguros por tipo."));

        // Verificar que el mock fue llamado
        verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    void testObtenerSegurosPorTipoInterruptedException() throws Exception {
        // Configurar el mock para lanzar una InterruptedException
        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("Operación interrumpida"));

        // Ejecutar el método y verificar la excepción
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> clientController.obtenerSegurosPorTipo("VIDA"));
        assertTrue(exception.getMessage().contains("Error obteniendo seguros por tipo."));

        // Verificar que el mock fue llamado
        verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }
}
