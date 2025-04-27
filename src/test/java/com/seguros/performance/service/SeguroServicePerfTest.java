package com.seguros.performance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
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

    private final static JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(
                    new HtmlReportGenerator(System.getProperty("user.dir") +
                            "/target/reports/perf-report.html"))
            .build();

    private String nombreUnico;
    private Long seguroId;

    @BeforeEach
    public void setup() {
        // Generar un nombre único utilizando UUID para evitar colisiones
        nombreUnico = "Seguro inicial " + System.currentTimeMillis() + "_" + java.util.UUID.randomUUID().toString();
        Seguro seguro = new Seguro(nombreUnico, "Cobertura estándar", TipoSeguro.VIDA, 1000.0);
        seguro = segurorepo.save(seguro); // Guardamos el seguro y obtenemos el ID generado
        seguroId = seguro.getId(); // Guardamos el ID del seguro para futuras pruebas
    }

    // Test de rendimiento para el método crearSeguro
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    @JUnitPerfTestRequirement(meanLatency = 500, allowedErrorPercentage = (float) 0.05)
    public void crearSeguroPerformanceTest() {
        // Generar un nuevo nombre único para cada ejecución del test
        String nombreUnicoTest = "Seguro inicial " + System.currentTimeMillis() + "_"
                + java.util.UUID.randomUUID().toString();
        seguroService.crearSeguro(nombreUnicoTest, "Cobertura básica", "VIDA", 1200.0);
    }

    // Test de rendimiento para el método editarSeguro
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    @JUnitPerfTestRequirement(meanLatency = 1000, allowedErrorPercentage = (float) 0.1)
    public void editarSeguroPerformanceTest() {
        // Editar un seguro creado previamente usando su ID
        boolean result = seguroService.editarSeguro(seguroId, nombreUnico,
                "Cobertura extendida", "VIDA", 1500.0);
        assert result; // Verifica que el seguro se editó correctamente
    }

    // Test de rendimiento para el método obtenerSeguroPorNombre
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    @JUnitPerfTestRequirement(meanLatency = 100, allowedErrorPercentage = (float) 0.05)
    public void obtenerSeguroPorNombrePerformanceTest() {
        // Buscar un seguro creado previamente por su nombre
        var seguro = seguroService.obtenerSeguroPorNombre(nombreUnico);
        assert seguro != null; // Asegura que el seguro se encontró correctamente
    }

    // Test de rendimiento para el método eliminarSeguro
    @Test
    @JUnitPerfTest(threads = 10, durationMs = 1000, rampUpPeriodMs = 500)
    @JUnitPerfTestRequirement(meanLatency = 100, allowedErrorPercentage = (float) 0.05)
    public void eliminarSeguroPerformanceTest() {
        boolean result = seguroService.eliminarSeguro(seguroId);
        assert result; // Verifica que el seguro fue eliminado exitosamente
    }

    // Test de rendimiento para el método obtenerTodosSeguros
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 3000, rampUpPeriodMs = 1000)
    @JUnitPerfTestRequirement(meanLatency = 700, allowedErrorPercentage = (float) 0.05)
    public void obtenerTodosSegurosPerformanceTest() {
        var seguros = seguroService.obtenerTodosSeguros();
        assert seguros != null && !seguros.isEmpty(); // Asegura que la lista de
        // seguros no está vacía
    }
}
