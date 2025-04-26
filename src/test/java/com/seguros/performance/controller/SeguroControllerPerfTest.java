package com.seguros.performance.controller;

import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.seguros.controller.SeguroController;
import com.seguros.model.Seguro;
import com.seguros.Service.SeguroService;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

@ExtendWith(JUnitPerfInterceptor.class)
public class SeguroControllerPerfTest {

    private SeguroController seguroController;
    private SeguroService seguroService;

    @BeforeEach
    void setUp() {
        // Mockear el servicio que el controller usa
        seguroService = Mockito.mock(SeguroService.class);
        seguroController = new SeguroController(seguroService);
    }

    // Test de rendimiento para crear un seguro
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    public void crearSeguroPerformanceTest() {
        // Simulamos la llamada al método crearSeguro
        ResponseEntity<String> response = seguroController.crearSeguro("Seguro de vida", "Cobertura completa", "VIDA",
                1000.0);
        assert response.getStatusCode().is2xxSuccessful();
    }

    // Test de rendimiento para editar un seguro
    // @Test
    // @JUnitPerfTest(threads = 2, durationMs = 1000, rampUpPeriodMs = 500)
    // @JUnitPerfTestRequirement(meanLatency = 1000, allowedErrorPercentage =
    // (float) 0.1)
    // public void editarSeguroPerformanceTest() {
    // // Simulamos la llamada al método editarSeguro
    // ResponseEntity<String> response = seguroController.editarSeguro(1L, "Seguro
    // de vida editado",
    // "Cobertura básica", "VIDA", 1200.0);
    // assert response.getStatusCode().is2xxSuccessful();
    // }

    // // Test de rendimiento para obtener un seguro por nombre
    // @Test
    // @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    // public void obtenerSeguroPorNombrePerformanceTest() {
    // // Simulamos la llamada al método obtenerSeguroPorNombre
    // ResponseEntity<Seguro> response =
    // seguroController.obtenerSeguroPorNombre("Seguro de vida");
    // assert response.getStatusCode().is2xxSuccessful();
    // }

    // // Test de rendimiento para eliminar un seguro
    // @Test
    // @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    // public void eliminarSeguroPerformanceTest() {
    // // Simulamos la llamada al método eliminarSeguro
    // ResponseEntity<String> response = seguroController.eliminarSeguro(1L);
    // assert response.getStatusCode().is2xxSuccessful();
    // }

    // Test de rendimiento para obtener todos los seguros
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    public void obtenerTodosSegurosPerformanceTest() {
        // Simulamos la llamada al método obtenerTodosSeguros
        ResponseEntity<List<Seguro>> response = seguroController.obtenerTodosSeguros();
        assert response.getStatusCode().is2xxSuccessful();
    }

    // Test de rendimiento para obtener seguros por tipo
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    public void obtenerSegurosPorTipoPerformanceTest() {
        // Simulamos la llamada al método obtenerSegurosPorTipo
        ResponseEntity<List<Seguro>> response = seguroController.obtenerSegurosPorTipo("VIDA");
        assert response.getStatusCode().is2xxSuccessful();
    }
}
