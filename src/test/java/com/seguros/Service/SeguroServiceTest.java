package com.seguros.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
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

    @Test
    void testObtenerTodosSeguros_Exitoso() {
        // Datos de prueba
        List<Seguro> segurosMock = new ArrayList<>();
        segurosMock.add(new Seguro("Seguro de Vida", "Cobertura total", TipoSeguro.VIDA, 1000.0));
        segurosMock.add(new Seguro("Seguro de Auto", "Cobertura contra daños", TipoSeguro.COCHE, 500.0));

        // Simulación del método findAll en el repositorio
        when(seguroRepository.findAll()).thenReturn(segurosMock);

        // Ejecución del método a probar
        List<Seguro> result = seguroService.obtenerTodosSeguros();

        // Verificaciones
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Seguro de Vida", result.get(0).getNombre());
        assertEquals("Seguro de Auto", result.get(1).getNombre());
        verify(seguroRepository, times(1)).findAll();
    }

    @Test
    void testObtenerTodosSeguros_ListaVacia() {
        // Simulación del método findAll en el repositorio con una lista vacía
        when(seguroRepository.findAll()).thenReturn(new ArrayList<>());

        // Ejecución del método a probar
        List<Seguro> result = seguroService.obtenerTodosSeguros();

        // Verificaciones
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(seguroRepository, times(1)).findAll();
    }

    @Test
    void testObtenerTodosSeguros_RepositorioNulo() {
        // Simulación del método findAll en el repositorio devolviendo null
        when(seguroRepository.findAll()).thenReturn(null);

        // Ejecución del método a probar
        List<Seguro> result = seguroService.obtenerTodosSeguros();

        // Verificaciones
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(seguroRepository, times(1)).findAll();
    }

    @Test
    void testObtenerSegurosPorTipo_ValidTipoSeguro() {
        // Mock del repositorio
        TipoSeguro tipoSeguro = TipoSeguro.VIDA;
        List<Seguro> mockSeguros = Arrays.asList(new Seguro(), new Seguro());
        when(seguroRepository.findByTipoSeguro(tipoSeguro)).thenReturn(mockSeguros);

        // Prueba
        List<Seguro> resultado = seguroService.obtenerSegurosPorTipo("VIDA");
        assertEquals(mockSeguros, resultado);

        // Verificación de interacción
        verify(seguroRepository, times(1)).findByTipoSeguro(tipoSeguro);
    }

    @Test
    void testObtenerSegurosPorTipo_InvalidTipoSeguro() {
        // Prueba de excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> seguroService.obtenerSegurosPorTipo("invalido"));

        assertEquals("El tipo de seguro 'invalido' no es válido. Los valores permitidos son: " +
                Arrays.toString(TipoSeguro.values()), exception.getMessage());
    }

    // ----------------- TESTS PARA editarSeguro -----------------

    @Test
    void testEditarSeguro_Exitoso() {
        Seguro existente = new Seguro("Original", "Desc", TipoSeguro.CASA, 100.0);
        when(seguroRepository.findById(1L)).thenReturn(java.util.Optional.of(existente));
        doAnswer(invocation -> null).when(seguroRepository).save(any(Seguro.class));

        boolean resultado = seguroService.editarSeguro(1L, "NuevoNombre", "NuevaDesc", "VIDA", 200.0);

        assertTrue(resultado);
        assertEquals("NuevoNombre", existente.getNombre());
        assertEquals("NuevaDesc", existente.getDescripcion());
        assertEquals(TipoSeguro.VIDA, existente.getTipoSeguro());
        assertEquals(200.0, existente.getPrecio());
        verify(seguroRepository, times(1)).save(existente);
    }

    @Test
    void testEditarSeguro_SeguroNoExiste() {
        when(seguroRepository.findById(99L)).thenReturn(java.util.Optional.empty());
        boolean resultado = seguroService.editarSeguro(99L, "Nombre", "Desc", "VIDA", 100.0);
        assertFalse(resultado);
    }

    @Test
    void testEditarSeguro_TipoInvalido() {
        when(seguroRepository.findById(1L)).thenReturn(java.util.Optional.of(new Seguro()));
        assertThrows(IllegalArgumentException.class, () ->
                seguroService.editarSeguro(1L, "Nombre", "Desc", "INVALIDO", 100.0));
    }

    // ----------------- TESTS PARA obtenerSeguroPorNombre -----------------

    @Test
    void testObtenerSeguroPorNombre_Encontrado() {
        Seguro seguro = new Seguro("Vida", "Cobertura", TipoSeguro.VIDA, 1000.0);
        when(seguroRepository.findByNombre("Vida")).thenReturn(seguro);

        Seguro resultado = seguroService.obtenerSeguroPorNombre("Vida");

        assertNotNull(resultado);
        assertEquals("Vida", resultado.getNombre());
    }

    @Test
    void testObtenerSeguroPorNombre_NoEncontrado() {
        when(seguroRepository.findByNombre("Inexistente")).thenReturn(null);
        Seguro resultado = seguroService.obtenerSeguroPorNombre("Inexistente");
        assertNull(resultado);
    }

    // ----------------- TESTS PARA eliminarSeguro -----------------

    @Test
    void testEliminarSeguro_Exitoso() {
        when(seguroRepository.existsById(1L)).thenReturn(true);
        doAnswer(invocation -> null).when(seguroRepository).deleteById(1L);

        boolean resultado = seguroService.eliminarSeguro(1L);

        assertTrue(resultado);
        verify(seguroRepository, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarSeguro_NoExiste() {
        when(seguroRepository.existsById(999L)).thenReturn(false);

        boolean resultado = seguroService.eliminarSeguro(999L);

        assertFalse(resultado);
        verify(seguroRepository, never()).deleteById(anyLong());
    }

    @Test
    void testEliminarSeguro_Excepcion() {
        when(seguroRepository.existsById(1L)).thenReturn(true);
        doAnswer(invocation -> {
            throw new RuntimeException("Error inesperado");
        }).when(seguroRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> seguroService.eliminarSeguro(1L));
    }
}
