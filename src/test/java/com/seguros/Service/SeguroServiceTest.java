package com.seguros.Service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;
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

    /*
     * @Test
     * void testObtenerTodosSeguros_ListaConSeguros() {
     * // Simulación de seguros en la base de datos
     * List<Seguro> segurosMock = List.of(
     * new Seguro("Seguro Vida", "Cobertura completa", TipoSeguro.VIDA, 1000.0),
     * new Seguro("Seguro Auto", "Protección contra accidentes", TipoSeguro.COCHE,
     * 500.0));
     * 
     * // Mockeamos el comportamiento del repositorio
     * when(seguroRepository.findAll()).thenReturn(segurosMock);
     * 
     * // Ejecutamos el método
     * List<String> nombresSeguros = seguroService.obtenerTodosSeguros();
     * 
     * // Validamos los resultados
     * assertEquals(2, nombresSeguros.size());
     * assertTrue(nombresSeguros.contains("Seguro Vida"));
     * assertTrue(nombresSeguros.contains("Seguro Auto"));
     * }
     * 
     * @Test
     * void testObtenerTodosSeguros_ListaVacia() {
     * // Mock de lista vacía en el repositorio
     * when(seguroRepository.findAll()).thenReturn(new ArrayList<>());
     * 
     * // Ejecutamos el método
     * List<String> nombresSeguros = seguroService.obtenerTodosSeguros();
     * 
     * // Validamos que la lista es vacía
     * assertNotNull(nombresSeguros);
     * assertTrue(nombresSeguros.isEmpty());
     * }
     * 
     * @Test
     * void testObtenerTodosSeguros_ListaNula() {
     * // Simulación de que el repositorio devuelve null
     * when(seguroRepository.findAll()).thenReturn(null);
     * 
     * // Ejecutamos el método
     * List<String> nombresSeguros = seguroService.obtenerTodosSeguros();
     * 
     * // Validamos que devuelve una lista vacía en lugar de fallar
     * assertNotNull(nombresSeguros);
     * assertTrue(nombresSeguros.isEmpty());
     * }
     * 
     * @Test
     * void testObtenerTodosSeguros_ListaConNombresNulos() {
     * // Simulación de seguros con nombres nulos o en blanco
     * List<Seguro> segurosMock = List.of(
     * new Seguro(null, "Cobertura completa", TipoSeguro.VIDA, 1000.0),
     * new Seguro(" ", "Protección contra accidentes", TipoSeguro.COCHE, 500.0),
     * new Seguro("Seguro Hogar", "Protección contra incendios", TipoSeguro.CASA,
     * 1200.0));
     * 
     * when(seguroRepository.findAll()).thenReturn(segurosMock);
     * 
     * // Ejecutamos el método
     * List<String> nombresSeguros = seguroService.obtenerTodosSeguros();
     * 
     * // Validamos que solo se agregue el nombre válido
     * assertEquals(1, nombresSeguros.size());
     * assertTrue(nombresSeguros.contains("Seguro Hogar"));
     * }
     */
}
