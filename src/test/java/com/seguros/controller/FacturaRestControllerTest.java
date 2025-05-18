package com.seguros.controller;

import com.seguros.Service.FacturaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class FacturaRestControllerTest {
    private FacturaService facturaServiceMock;
    private FacturaRestController controller;

    @BeforeEach
    public void setUp() {
        facturaServiceMock = mock(FacturaService.class);
        controller = new FacturaRestController(facturaServiceMock);
    }

    @Test
    public void testDownloadFactura() throws IOException {
        // Arrange
        Long clienteId = 1L;
        String contenidoEsperado = "Factura de prueba\nCliente ID: " + clienteId;
        Path tempFile = Files.createTempFile("factura_", ".txt");
        Files.write(tempFile, contenidoEsperado.getBytes(), StandardOpenOption.WRITE);

        when(facturaServiceMock.generarFacturaParaCliente(clienteId)).thenReturn(tempFile);

        // Act
        ResponseEntity<Resource> response = controller.download(clienteId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getHeaders().getContentDisposition().getFilename().startsWith("factura_"));
        assertEquals("text/plain", response.getHeaders().getContentType().toString());

        byte[] responseBytes = response.getBody().getInputStream().readAllBytes();
        String contenidoObtenido = new String(responseBytes);
        assertEquals(contenidoEsperado, contenidoObtenido);

        // Cleanup
        Files.deleteIfExists(tempFile);
    }
}
