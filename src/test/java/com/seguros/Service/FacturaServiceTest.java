package com.seguros.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.seguros.model.Factura;
import com.seguros.model.Seguro;
import com.seguros.repository.FacturaRepository;
import com.seguros.repository.SeguroRepository;

public class FacturaServiceTest {

    private FacturaRepository facturaRepository;
    private FacturaService facturaService;
    private SeguroService seguroService;

    @BeforeEach
    void setUp() {
        facturaRepository = mock(FacturaRepository.class);
        seguroService = mock(SeguroService.class);
        facturaService = new FacturaService(facturaRepository, seguroService);
    }

    @Test
    void testGetFacturasByClienteId_CuandoExistenFacturas() {
        // Arrange
        Long clienteId = 1L;
        List<Factura> facturas = Arrays.asList(new Factura(), new Factura());
        when(facturaRepository.findByIdCliente(clienteId)).thenReturn(facturas);

        // Act
        List<Factura> resultado = facturaService.getFacturasByClienteId(clienteId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size(), "Debe retornar la cantidad correcta de facturas");
    }

    @Test
    void testGetFacturasByClienteId_CuandoNoExistenFacturas() {
        // Arrange
        Long clienteId = 2L;
        when(facturaRepository.findByIdCliente(clienteId)).thenReturn(List.of());

        // Act
        List<Factura> resultado = facturaService.getFacturasByClienteId(clienteId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "Debe retornar una lista vacía si no hay facturas");
    }

    @Test
    void testGenerarFacturaParaCliente() throws IOException {
        Long clienteId = 1L;
        Seguro seguro = mock(Seguro.class);
        when(seguro.getPrecio()).thenReturn((double) 100.0f);
        when(seguro.getNombre()).thenReturn("Seguro Vida");
        when(seguroService.obtenerSegurosPorCliente(clienteId)).thenReturn(List.of(seguro));

        Factura factura = new Factura();
        factura.setId(123L);
        factura.setIdCliente(clienteId);
        factura.setCantidad(100f);
        factura.setEstado("PENDIENTE");
        factura.setFecha(LocalDate.now().toString());

        // Simula que el repository asigna el ID al guardar
        when(facturaRepository.save(any(Factura.class))).thenAnswer(invocation -> {
            Factura f = invocation.getArgument(0);
            f.setId(123L);
            return f;
        });

        Path expectedPath = Paths.get("facturas/factura_123.txt").toAbsolutePath();

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
            filesMock.when(() -> Files.write(any(Path.class), any(byte[].class))).thenReturn(expectedPath);

            Path result = facturaService.generarFacturaParaCliente(clienteId);

            assertEquals(expectedPath, result);
            verify(seguroService).obtenerSegurosPorCliente(clienteId);
            verify(facturaRepository).save(any(Factura.class));
            filesMock.verify(() -> Files.createDirectories(Paths.get("facturas")));
            filesMock
                    .verify(() -> Files.write(eq(Paths.get("facturas").resolve("factura_123.txt")), any(byte[].class)));
        }
    }

    @Test
    void testGenerarFacturaParaCliente_SinSeguros() throws IOException {
        Long clienteId = 2L;
        when(seguroService.obtenerSegurosPorCliente(clienteId)).thenReturn(Collections.emptyList());

        Factura factura = new Factura();
        factura.setId(456L);
        factura.setIdCliente(clienteId);
        factura.setCantidad(0f);
        factura.setEstado("PENDIENTE");
        factura.setFecha(LocalDate.now().toString());

        when(facturaRepository.save(any(Factura.class))).thenAnswer(invocation -> {
            Factura f = invocation.getArgument(0);
            f.setId(456L);
            return f;
        });

        Path expectedPath = Paths.get("facturas/factura_456.txt").toAbsolutePath();

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
            filesMock.when(() -> Files.write(any(Path.class), any(byte[].class))).thenReturn(expectedPath);

            Path result = facturaService.generarFacturaParaCliente(clienteId);

            assertEquals(expectedPath, result);
            verify(seguroService).obtenerSegurosPorCliente(clienteId);
            verify(facturaRepository).save(any(Factura.class));
            filesMock.verify(() -> Files.createDirectories(Paths.get("facturas")));
            filesMock
                    .verify(() -> Files.write(eq(Paths.get("facturas").resolve("factura_456.txt")), any(byte[].class)));
        }
    }

}
