package com.seguros.Service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

import com.seguros.model.Seguro;
import com.seguros.repository.SeguroRepository;

public class SeguroServiceTest {

    @Mock
    private SeguroRepository seguroRepository;

    @InjectMocks
    private SeguroService seguroService;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearSeguro_Exitoso() {
        // Datos de prueba
        String nombre = "Seguro de Vida";
        String descripcion = "Cobertura total";
        String tipoSeguro = "VIDA";
        Double precio = 1000.0;

        // Simulación del método save en el repositorio
        doAnswer(invocation -> null).when(seguroRepository).save(any(Seguro.class));

        // Ejecución del método a probar
        assertDoesNotThrow(() -> seguroService.crearSeguro(nombre, descripcion, tipoSeguro, precio));

        // Verificación de que el seguro fue guardado correctamente
        verify(seguroRepository, times(1)).save(any(Seguro.class));
    }

    @Test
    void testCrearSeguro_TipoSeguroInvalido() {
        String nombre = "Seguro de Auto";
        String descripcion = "Cobertura contra daños";
        String tipoSeguro = "INVALIDO";
        Double precio = 500.0;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> seguroService.crearSeguro(nombre, descripcion, tipoSeguro, precio));

        assertTrue(exception.getMessage().contains("no es válido"));
    }

    @Test
    void testCrearSeguro_RepositorioNoInicializado() {
        seguroService.segurorepo = null;

        String nombre = "Seguro de Hogar";
        String descripcion = "Protección contra incendios";
        String tipoSeguro = "CASA";
        Double precio = 1200.0;

        Exception exception = assertThrows(IllegalStateException.class,
                () -> seguroService.crearSeguro(nombre, descripcion, tipoSeguro, precio));

        assertEquals("El repositorio segurorepo no está inicializado", exception.getMessage());
    }

    @Test
    void testRestaurarEspacios_Exitoso() {
        String input = "Hola;mundo;esto;es;una;prueba";
        String expected = "Hola mundo esto es una prueba";

        String result = SeguroService.restaurarEspacios(input);

        assertEquals(expected, result);
    }

    @Test
    void testRestaurarEspacios_TextoNulo() {
        String result = SeguroService.restaurarEspacios(null);

        assertNull(result);
    }

    @Test
    void testRestaurarEspacios_SinCaracteresEspeciales() {
        String input = "Texto sin punto y coma";
        String expected = "Texto sin punto y coma"; // No debe modificarse

        String result = SeguroService.restaurarEspacios(input);

        assertEquals(expected, result);
    }

    @Test
    void testRestaurarEspacios_CadenaVacia() {
        String input = "";
        String expected = ""; // No debe modificarse

        String result = SeguroService.restaurarEspacios(input);

        assertEquals(expected, result);
    }
}
