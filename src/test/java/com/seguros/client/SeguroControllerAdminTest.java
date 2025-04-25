package com.seguros.client;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import javax.swing.JOptionPane;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import org.mockito.Mock;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.seguros.model.Seguro;

public class SeguroControllerAdminTest {
        @Mock
        private HttpClient httpClientMock;

        @Mock
        private HttpResponse<String> responseMock;

        private SeguroControllerAdmin adminController;

        @BeforeEach
        @SuppressWarnings("unused")
        void setUp() {
                MockitoAnnotations.openMocks(this);
                adminController = new SeguroControllerAdmin(httpClientMock, "localhost", "8080");
        }

        @Test
        void testConstructorOriginal() {
                // Crear una instancia del controlador usando el constructor original
                SeguroControllerAdmin controller = new SeguroControllerAdmin("localhost",
                                "8080");

                // Verificar que la URL base se construyó correctamente
                String expectedBaseUrl = "http://localhost:8080";
                assertEquals(expectedBaseUrl, controller.getBaseUrl());

                // Verificar que el HttpClient no sea nulo
                assertNotNull(controller.getHttpClient());
        }

        @Test
        void testCrearSeguroCodigo200() throws Exception {
                when(responseMock.statusCode()).thenReturn(200);
                when(responseMock.body()).thenReturn("");
                when(httpClientMock.send(any(HttpRequest.class),
                                any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);

                assertDoesNotThrow(() -> adminController.crearSeguro("s", "s", "VIDA", 1.0));

                assertEquals(200, responseMock.statusCode());

                verify(httpClientMock).send(any(HttpRequest.class),
                                any(HttpResponse.BodyHandler.class));
        }

        @Test
        void testCrearSeguroCodigo400() throws Exception {
                try (var mockedJOptionPane = mockStatic(JOptionPane.class)) {
                        mockedJOptionPane.when(() -> JOptionPane.showMessageDialog(any(), any(), any(), anyInt()))
                                        .thenAnswer(invocation -> null);

                        // Configurar el mock para devolver un código 400
                        when(responseMock.statusCode()).thenReturn(400);
                        when(responseMock.body()).thenReturn("Datos inválidos");
                        when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                                        .thenReturn(responseMock);

                        // Ejecutar el método y verificar la excepción
                        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                        () -> adminController.crearSeguro("Seguro1", "Cobertura", "casa", 500.0));
                        assertTrue(exception.getMessage().contains("Error al crear el seguro: Datos inválidos"));

                        // Verificar que el mock fue llamado
                        verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
                }
        }

        @Test
        void testCrearSeguroCodigo500() throws Exception {
                when(responseMock.statusCode()).thenReturn(500);
                when(httpClientMock.send(any(HttpRequest.class),
                                any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> adminController.crearSeguro("Seguro2", "Cobertura", "COCHE", 2000.0));
                assertEquals("Error interno del servidor.", exception.getMessage());

                verify(httpClientMock).send(any(HttpRequest.class),
                                any(HttpResponse.BodyHandler.class));
        }

        @Test
        void testCrearSeguroCodigoDesconocido() throws Exception {
                when(responseMock.statusCode()).thenReturn(418);
                when(httpClientMock.send(any(HttpRequest.class),
                                any(HttpResponse.BodyHandler.class))).thenReturn(responseMock);

                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> adminController.crearSeguro("Seguro3", "Cobertura", "CASA", 1500.0));
                assertTrue(exception.getMessage().contains("Fallo al crear el seguro con el código de estado: 418"));

                verify(httpClientMock).send(any(HttpRequest.class),
                                any(HttpResponse.BodyHandler.class));
        }

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

        @Test
        void testListaNombreSegurosCodigo200() throws Exception {
                // Configurar el mock para devolver un código 200 y un cuerpo válido
                String jsonResponse = "[{\"id\":1,\"nombre\":\"Seguro1\"},{\"id\":2,\"nombre\":\"Seguro2\"}]";
                when(responseMock.statusCode()).thenReturn(200);
                when(responseMock.body()).thenReturn(jsonResponse);
                when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                                .thenReturn(responseMock);

                // Ejecutar el método
                List<Seguro> seguros = adminController.listaNombreSeguros();

                // Verificar el resultado
                assertEquals(2, seguros.size());
                assertEquals("Seguro1", seguros.get(0).getNombre());
                assertEquals("Seguro2", seguros.get(1).getNombre());

                // Verificar que el mock fue llamado
                verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        }

        @Test
        void testListaNombreSegurosCodigo404() throws Exception {
                // Configurar el mock para devolver un código 404
                when(responseMock.statusCode()).thenReturn(404);
                when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                                .thenReturn(responseMock);

                // Ejecutar el método y verificar la excepción
                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> adminController.listaNombreSeguros());
                assertEquals("No se encontraron seguros en el servidor.", exception.getMessage());

                // Verificar que el mock fue llamado
                verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        }

        @Test
        void testListaNombreSegurosCodigoDesconocido() throws Exception {
                // Configurar el mock para devolver un código desconocido
                when(responseMock.statusCode()).thenReturn(500);
                when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                                .thenReturn(responseMock);

                // Ejecutar el método y verificar la excepción
                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> adminController.listaNombreSeguros());
                assertEquals("Error al obtener los seguros: Código de estado 500", exception.getMessage());

                // Verificar que el mock fue llamado
                verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        }

        @Test
        void testListaNombreSegurosIOException() throws Exception {
                // Configurar el mock para lanzar una IOException
                when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                                .thenThrow(new IOException("Error de conexión"));

                // Ejecutar el método y verificar la excepción
                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> adminController.listaNombreSeguros());
                assertTrue(exception.getMessage().contains("Error de entrada/salida al obtener los seguros."));

                // Verificar que el mock fue llamado
                verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        }

        @Test
        void testListaNombreSegurosInterruptedException() throws Exception {
                // Configurar el mock para lanzar una InterruptedException
                when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                                .thenThrow(new InterruptedException("Operación interrumpida"));

                // Ejecutar el método y verificar la excepción
                RuntimeException exception = assertThrows(RuntimeException.class,
                                () -> adminController.listaNombreSeguros());
                assertTrue(exception.getMessage().contains("La solicitud fue interrumpida."));

                // Verificar que el mock fue llamado
                verify(httpClientMock).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        }
}
