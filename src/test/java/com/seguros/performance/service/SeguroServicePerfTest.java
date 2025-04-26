package com.seguros.performance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestActiveConfig;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import com.seguros.Service.SeguroService;
import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;
import com.seguros.repository.SeguroRepository;

@SpringBootTest
public class SeguroServicePerfTest {

    @Autowired
    private SeguroService seguroService;

    @Autowired
    private SeguroRepository segurorepo;

    @JUnitPerfTestActiveConfig
    private final static JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(
                    new HtmlReportGenerator(System.getProperty("user.dir") + "/target/reports/perf-report.html"))
            .build();

    @BeforeEach
    public void setup() {
        String nombreUnico = "Seguro inicial " + System.currentTimeMillis();
        Seguro seguro = new Seguro(nombreUnico, "Cobertura estándar", TipoSeguro.VIDA, 1000.0);
        segurorepo.save(seguro);
    }

    // Test de rendimiento para el método crearSeguro
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 2000, rampUpPeriodMs = 500)
    @JUnitPerfTestRequirement(meanLatency = 500, allowedErrorPercentage = (float) 0.05)
    public void crearSeguroPerformanceTest() {
        String nombreUnico = "Seguro de vida " + System.currentTimeMillis();
        seguroService.crearSeguro(nombreUnico, "Cobertura básica", "VIDA", 1200.0);
    }

    // Test de rendimiento para el método editarSeguro
    // @Test
    // @JUnitPerfTest(threads = 10, durationMs = 2000, rampUpPeriodMs = 500)
    // @JUnitPerfTestRequirement(meanLatency = 1000, allowedErrorPercentage =
    // (float) 0.1)
    // public void editarSeguroPerformanceTest() {
    // boolean result = seguroService.editarSeguro(1L, "Seguro actualizado",
    // "Cobertura extendida", "VIDA", 1500.0);
    // assert result; // Verifica que el seguro se editó correctamente
    // }

    // Test de rendimiento para el método obtenerSeguroPorNombre
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 2000, rampUpPeriodMs = 500)
    @JUnitPerfTestRequirement(meanLatency = 300, allowedErrorPercentage = (float) 0.05)
    public void obtenerSeguroPorNombrePerformanceTest() {
        var seguro = seguroService.obtenerSeguroPorNombre("Seguro de vida");
        assert seguro != null; // Asegura que el seguro se encontró correctamente
    }

    // Test de rendimiento para el método eliminarSeguro
    // @Test
    // @JUnitPerfTest(threads = 10, durationMs = 2000, rampUpPeriodMs = 500)
    // @JUnitPerfTestRequirement(meanLatency = 500, allowedErrorPercentage = (float)
    // 0.05)
    // public void eliminarSeguroPerformanceTest() {
    // boolean result = seguroService.eliminarSeguro(1L);
    // assert result; // Verifica que el seguro fue eliminado exitosamente
    // }

    // Test de rendimiento para el método obtenerTodosSeguros
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 3000, rampUpPeriodMs = 1000)
    @JUnitPerfTestRequirement(meanLatency = 700, allowedErrorPercentage = (float) 0.05)
    public void obtenerTodosSegurosPerformanceTest() {
        var seguros = seguroService.obtenerTodosSeguros();
        assert seguros != null && !seguros.isEmpty(); // Asegura que la lista de seguros no está vacía
    }
}
