package com.seguros.performance.controller;

import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
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

    private final static JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(
                    new HtmlReportGenerator(System.getProperty("user.dir") +
                            "/target/reports/perf-report.html"))
            .build();

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

    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    public void editarSeguroPerformanceTest() {
        // Simulamos la llamada al método editarSeguro
        seguroController.editarSeguro(1L, "Seguro de vida editado",
                "Cobertura básica", "VIDA", 1200.0);
    }

    // Test de rendimiento para obtener un seguro por nombre
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    public void obtenerSeguroPorNombrePerformanceTest() {
        // Simulamos la llamada al método obtenerSeguroPorNombre
        seguroController.obtenerSeguroPorNombre("Seguro de vida");
    }

    // Test de rendimiento para eliminar un seguro
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    public void eliminarSeguroPerformanceTest() {
        // Simulamos la llamada al método eliminarSeguro
        seguroController.eliminarSeguro(1L);
    }

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
