package com.seguros.performance.controller;

import com.github.noconnor.junitperf.JUnitPerfInterceptor;
import com.github.noconnor.junitperf.JUnitPerfReportingConfig;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
import com.seguros.controller.AdminController;
import com.seguros.model.Administrador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.ResponseEntity;

@ExtendWith(JUnitPerfInterceptor.class)
public class AdminControllerPerfTest {

    private AdminController adminController;

    private final static JUnitPerfReportingConfig PERF_CONFIG = JUnitPerfReportingConfig.builder()
            .reportGenerator(
                    new HtmlReportGenerator(System.getProperty("user.dir") +
                            "/target/reports/perf-report.html"))
            .build();

    @BeforeEach
    void setUp() {
        // Mockear el repository que el controller usa
        adminController = new AdminController();
    }

    // Test de rendimiento para login
    @Disabled
    @Test
    @JUnitPerfTest(threads = 2, durationMs = 1000, rampUpPeriodMs = 500)
    @JUnitPerfTestRequirement(meanLatency = 500, allowedErrorPercentage = (float) 0.1) // Ajuste de latencia
    public void loginPerformanceTest() {
        // Crear un administrador simulado para login
        Administrador admin = new Administrador();
        admin.setEmail("admin@gmail.com");
        admin.setPassword("1234");
        admin.setNombre("admin1234");

        // Simulamos la llamada al método login
        ResponseEntity<?> response = adminController.login(admin);
        assert response.getStatusCode().is2xxSuccessful(); // Verifica que el login fue exitoso
    }

    // Test de rendimiento para logout
    @Test
    @JUnitPerfTest(threads = 5, durationMs = 1000, rampUpPeriodMs = 500)
    @JUnitPerfTestRequirement(meanLatency = 500, allowedErrorPercentage = (float) 0.1) // Ajuste de latencia
    public void logoutPerformanceTest() {
        // Simulamos la llamada al método logout
        ResponseEntity<String> response = adminController.logout();
        assert response.getStatusCode().is2xxSuccessful(); // Verifica que el logout fue exitoso
    }
}
