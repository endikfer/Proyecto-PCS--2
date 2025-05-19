package com.seguros.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import com.seguros.model.Cliente;
import com.seguros.model.Seguro;
import com.seguros.model.SeguroCasa;
import com.seguros.model.SeguroCoche;
import com.seguros.model.SeguroVida;
import com.seguros.model.TipoSeguro;
import com.seguros.repository.SeguroRepository;
import com.seguros.repository.SeguroVidaRepository;
import com.seguros.repository.ClienteRepository;
import com.seguros.repository.SeguroCasaRepository;
import com.seguros.repository.SeguroCocheRepository;

public class SeguroServiceTest {

    @Mock
    private SeguroRepository segurorepo;

    @Mock
    private ClienteRepository clienterepo;

    @InjectMocks
    private SeguroService seguroService;

    @Mock
    private SeguroCocheRepository seguroCocheRepository;

    @Mock
    private SeguroVidaRepository seguroVidaRepository;

    @Mock
    private SeguroCasaRepository seguroCasaRepository;

    @BeforeEach
    void setUp() {
        segurorepo = mock(SeguroRepository.class);
        clienterepo = mock(ClienteRepository.class);
        seguroCocheRepository = mock(SeguroCocheRepository.class);
        seguroVidaRepository = mock(SeguroVidaRepository.class);
        seguroCasaRepository = mock(SeguroCasaRepository.class);
        seguroService = new SeguroService(segurorepo, clienterepo, seguroCocheRepository, seguroCasaRepository, seguroVidaRepository);
    }

    @Test
    void testCrearSeguro_Exitoso() {
        // Datos de prueba
        String nombre = "Seguro de Vida";
        String descripcion = "Cobertura total";
        String tipoSeguro = "VIDA";
        Double precio = 1000.0;

        // Simulación del método save en el repositorio
        doAnswer(invocation -> null).when(segurorepo).save(any(Seguro.class));

        // Ejecución del método a probar
        assertDoesNotThrow(() -> seguroService.crearSeguro(nombre, descripcion, tipoSeguro, precio));

        // Verificación de que el seguro fue guardado correctamente
        verify(segurorepo, times(1)).save(any(Seguro.class));
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
        SeguroService servicioSinRepo = new SeguroService(null, clienterepo, seguroCocheRepository, seguroCasaRepository, seguroVidaRepository);

        Exception ex = assertThrows(IllegalStateException.class,
                () -> servicioSinRepo.crearSeguro("Seguro de Hogar",
                        "Protección contra incendios",
                        "CASA",
                        1200.0));

        assertEquals("El repositorio segurorepo no está inicializado", ex.getMessage());
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
        when(segurorepo.findAll()).thenReturn(segurosMock);

        // Ejecución del método a probar
        List<Seguro> result = seguroService.obtenerTodosSeguros();

        // Verificaciones
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Seguro de Vida", result.get(0).getNombre());
        assertEquals("Seguro de Auto", result.get(1).getNombre());
        verify(segurorepo, times(1)).findAll();
    }

    @Test
    void testObtenerTodosSeguros_ListaVacia() {
        // Simulación del método findAll en el repositorio con una lista vacía
        when(segurorepo.findAll()).thenReturn(new ArrayList<>());

        // Ejecución del método a probar
        List<Seguro> result = seguroService.obtenerTodosSeguros();

        // Verificaciones
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(segurorepo, times(1)).findAll();
    }

    @Test
    void testObtenerTodosSeguros_RepositorioNulo() {
        // Simulación del método findAll en el repositorio devolviendo null
        when(segurorepo.findAll()).thenReturn(null);

        // Ejecución del método a probar
        List<Seguro> result = seguroService.obtenerTodosSeguros();

        // Verificaciones
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(segurorepo, times(1)).findAll();
    }

    @Test
    void testObtenerSegurosPorTipo_ValidTipoSeguro() {
        // Mock del repositorio
        TipoSeguro tipoSeguro = TipoSeguro.VIDA;
        List<Seguro> mockSeguros = Arrays.asList(new Seguro(), new Seguro());
        when(segurorepo.findByTipoSeguro(tipoSeguro)).thenReturn(mockSeguros);

        // Prueba
        List<Seguro> resultado = seguroService.obtenerSegurosPorTipo("VIDA");
        assertEquals(mockSeguros, resultado);

        // Verificación de interacción
        verify(segurorepo, times(1)).findByTipoSeguro(tipoSeguro);
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
        Seguro seguro = new Seguro("Antiguo", "Vieja desc", TipoSeguro.COCHE, 500.0);
        when(segurorepo.findById(anyLong())).thenReturn(Optional.of(seguro));

        boolean resultado = seguroService.editarSeguro(1L, "Nuevo", "Nueva desc", "VIDA", 1500.0);

        assertTrue(resultado);
        assertEquals("Nuevo", seguro.getNombre());
        assertEquals("Nueva desc", seguro.getDescripcion());
        assertEquals(TipoSeguro.VIDA, seguro.getTipoSeguro());
        assertEquals(1500.0, seguro.getPrecio());
        verify(segurorepo).save(seguro);
    }

    @Test
    void testEditarSeguro_SeguroNoExiste() {
        when(segurorepo.findById(99L)).thenReturn(java.util.Optional.empty());
        boolean resultado = seguroService.editarSeguro(99L, "Nombre", "Desc", "VIDA", 100.0);
        assertFalse(resultado);
    }

    @Test
    void testEditarSeguro_TipoInvalido() {
        when(segurorepo.findById(1L)).thenReturn(java.util.Optional.of(new Seguro()));
        assertThrows(IllegalArgumentException.class,
                () -> seguroService.editarSeguro(1L, "Nombre", "Desc", "INVALIDO", 100.0));
    }

    // ----------------- TESTS PARA obtenerSeguroPorNombre -----------------

    @Test
    void testObtenerSeguroPorNombre_Encontrado() {
        Seguro seguro = new Seguro("Vida", "Cobertura", TipoSeguro.VIDA, 1000.0);
        when(segurorepo.findByNombre("Vida")).thenReturn(seguro);

        Seguro resultado = seguroService.obtenerSeguroPorNombre("Vida");

        assertNotNull(resultado);
        assertEquals("Vida", resultado.getNombre());
    }

    @Test
    void testObtenerSeguroPorNombre_NoEncontrado() {
        when(segurorepo.findByNombre("Inexistente")).thenReturn(null);
        Seguro resultado = seguroService.obtenerSeguroPorNombre("Inexistente");
        assertNull(resultado);
    }

    // ----------------- TESTS PARA eliminarSeguro -----------------

    @Test
    void testEliminarSeguro_NoExiste() {
        when(segurorepo.existsById(999L)).thenReturn(false);

        boolean resultado = seguroService.eliminarSeguro(999L);

        assertFalse(resultado);
        verify(segurorepo, never()).deleteById(anyLong());
    }

    @Test
    void testEliminarSeguro_Exitoso() {
        Seguro seguro = new Seguro("Auto", "Cobertura completa", TipoSeguro.COCHE, 800.0);
        when(segurorepo.findById(1L)).thenReturn(java.util.Optional.of(seguro));

        boolean resultado = seguroService.eliminarSeguro(1L);

        assertTrue(resultado);
        verify(segurorepo, times(1)).delete(seguro);
    }

    // ----------------- TESTS PARA seleccionarSeguro -----------------

    @Test
    void testSeleccionarSeguro_Exitoso() {
        // Datos de prueba
        Long seguroId = 1L;
        Long clienteId = 1L;

        // Mock de seguro y cliente
        Seguro seguro = new Seguro("Seguro de Vida", "Cobertura total", TipoSeguro.VIDA, 1000.0);
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        // Simulación de los métodos findById en los repositorios
        when(segurorepo.findById(seguroId)).thenReturn(Optional.of(seguro));
        when(clienterepo.findById(clienteId)).thenReturn(Optional.of(cliente));

        // Ejecución del método a probar
        boolean resultado = seguroService.seleccionarSeguro(seguroId, clienteId);

        // Verificación
        assertTrue(resultado);
        assertEquals(seguro, cliente.getSeguroSeleccionado());
        verify(clienterepo, times(1)).save(cliente);
    }

    @Test
    void testSeleccionarSeguro_SeguroNoEncontrado() {
        Long seguroId = 1L;
        Long clienteId = 1L;

        // Mock del cliente sin seguro
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        // Simulación del método findById en el repositorio de seguro
        when(segurorepo.findById(seguroId)).thenReturn(Optional.empty());
        when(clienterepo.findById(clienteId)).thenReturn(Optional.of(cliente));

        // Ejecución del método a probar
        boolean resultado = seguroService.seleccionarSeguro(seguroId, clienteId);

        // Verificación
        assertFalse(resultado);
        verify(clienterepo, never()).save(any(Cliente.class));
    }

    @Test
    void testSeleccionarSeguro_ClienteNoEncontrado() {
        Long seguroId = 1L;
        Long clienteId = 1L;

        // Mock del seguro
        Seguro seguro = new Seguro("Seguro de Auto", "Cobertura contra daños", TipoSeguro.COCHE, 500.0);

        // Simulación del método findById en el repositorio de cliente
        when(segurorepo.findById(seguroId)).thenReturn(Optional.of(seguro));
        when(clienterepo.findById(clienteId)).thenReturn(Optional.empty());

        // Ejecución del método a probar
        boolean resultado = seguroService.seleccionarSeguro(seguroId, clienteId);

        // Verificación
        assertFalse(resultado);
        verify(clienterepo, never()).save(any(Cliente.class));
    }

    @Test
    void testSeleccionarSeguro_SeguroYClienteNoEncontrados() {
        Long seguroId = 1L;
        Long clienteId = 1L;

        // Simulación del método findById en los repositorios
        when(segurorepo.findById(seguroId)).thenReturn(Optional.empty());
        when(clienterepo.findById(clienteId)).thenReturn(Optional.empty());

        // Ejecución del método a probar
        boolean resultado = seguroService.seleccionarSeguro(seguroId, clienteId);

        // Verificación
        assertFalse(resultado);
        verify(clienterepo, never()).save(any(Cliente.class));
    }

    @Test
    void testObtenerPorCliente_ReturnsSeguros() {
        Long clienteId = 1L;
        Seguro seguro1 = new Seguro("Seguro1", "Desc1", TipoSeguro.CASA, 100.0);
        Seguro seguro2 = new Seguro("Seguro2", "Desc2", TipoSeguro.VIDA, 200.0);
        List<Seguro> seguros = Arrays.asList(seguro1, seguro2);

        when(segurorepo.findByClienteId(clienteId)).thenReturn(seguros);

        List<Seguro> result = seguroService.obtenerPorCliente(clienteId);

        assertEquals(2, result.size());
        assertSame(seguro1, result.get(0));
        assertSame(seguro2, result.get(1));
        verify(segurorepo).findByClienteId(clienteId);
    }

    @Test
    void testObtenerPorCliente_ReturnsEmptyList() {
        Long clienteId = 2L;
        when(segurorepo.findByClienteId(clienteId)).thenReturn(Collections.emptyList());

        List<Seguro> result = seguroService.obtenerPorCliente(clienteId);

        assertTrue(result.isEmpty());
        verify(segurorepo).findByClienteId(clienteId);
    }

    @Test
    void testContarClientesPorSeguro_ReturnsCounts() {
        Seguro seguro1 = new Seguro("Seguro1", "Desc1", TipoSeguro.CASA, 100.0);
        seguro1.setId(1L);
        Seguro seguro2 = new Seguro("Seguro2", "Desc2", TipoSeguro.VIDA, 200.0);
        seguro2.setId(2L);

        when(segurorepo.findAll()).thenReturn(Arrays.asList(seguro1, seguro2));
        when(clienterepo.countBySeguroSeleccionado_Id(1L)).thenReturn(3L);
        when(clienterepo.countBySeguroSeleccionado_Id(2L)).thenReturn(0L);

        List<ClientesPorSeguro> result = seguroService.contarClientesPorSeguro();

        assertEquals(2, result.size());
        assertEquals("Seguro1", result.get(0).getNombreSeguro());
        assertEquals(3L, result.get(0).getCantidadClientes());
        assertEquals("Seguro2", result.get(1).getNombreSeguro());
        assertEquals(0L, result.get(1).getCantidadClientes());
        verify(segurorepo).findAll();
        verify(clienterepo).countBySeguroSeleccionado_Id(1L);
        verify(clienterepo).countBySeguroSeleccionado_Id(2L);
    }

    @Test
    void testContarClientesPorSeguro_EmptySeguros() {
        when(segurorepo.findAll()).thenReturn(Collections.emptyList());

        List<ClientesPorSeguro> result = seguroService.contarClientesPorSeguro();

        assertTrue(result.isEmpty());
        verify(segurorepo).findAll();
        verifyNoInteractions(clienterepo);
    }

    @Test
    void testObtenerSegurosPorCliente_ReturnsSeguros() {
        Long clienteId = 5L;
        Seguro seguro = new Seguro("SeguroX", "DescX", TipoSeguro.COCHE, 300.0);
        List<Seguro> seguros = Collections.singletonList(seguro);

        when(segurorepo.findByClienteId(clienteId)).thenReturn(seguros);

        List<Seguro> result = seguroService.obtenerSegurosPorCliente(clienteId);

        assertEquals(1, result.size());
        assertSame(seguro, result.get(0));
        verify(segurorepo).findByClienteId(clienteId);
    }

    @Test
    void testObtenerSegurosPorCliente_ReturnsEmptyList() {
        Long clienteId = 6L;
        when(segurorepo.findByClienteId(clienteId)).thenReturn(Collections.emptyList());

        List<Seguro> result = seguroService.obtenerSegurosPorCliente(clienteId);

        assertTrue(result.isEmpty());
        verify(segurorepo).findByClienteId(clienteId);
    }

    @Test
    void testGuardarSeguroCoche() {
        Seguro seguro = new Seguro("Auto", "Cobertura total", TipoSeguro.COCHE, 1000.0);
        SeguroCoche seguroCoche = new SeguroCoche(seguro, "1234ABC", "ModelX", "Tesla");

        when(seguroCocheRepository.save(any(SeguroCoche.class))).thenReturn(seguroCoche);

        SeguroCoche result = seguroService.guardarSeguroCoche(seguro, "1234ABC", "ModelX", "Tesla");

        assertNotNull(result);
        assertEquals("1234ABC", result.getMatricula());
        assertEquals("ModelX", result.getModelo());
        assertEquals("Tesla", result.getMarca());
        assertEquals(seguro, result.getSeguro());
        verify(seguroCocheRepository, times(1)).save(any(SeguroCoche.class));
    }

    @Test
    void testGuardarSeguroVida() {
        Seguro seguro = new Seguro("Vida", "Cobertura vida", TipoSeguro.VIDA, 2000.0);
        SeguroVida seguroVida = new SeguroVida(seguro, 35, "Juan, Ana");

        when(seguroVidaRepository.save(any(SeguroVida.class))).thenReturn(seguroVida);

        SeguroVida result = seguroService.guardarSeguroVida(seguro, 35, "Juan, Ana");

        assertNotNull(result);
        assertEquals(35, result.getEdadAsegurado());
        assertEquals("Juan, Ana", result.getBeneficiarios());
        assertEquals(seguro, result.getSeguro());
        verify(seguroVidaRepository, times(1)).save(any(SeguroVida.class));
    }

    @Test
    void testGuardarSeguroCasa() {
        Seguro seguro = new Seguro("Casa", "Cobertura casa", TipoSeguro.CASA, 3000.0);
        SeguroCasa seguroCasa = new SeguroCasa(seguro, "Calle Falsa 123", 250000.0, "Piso");

        when(seguroCasaRepository.save(any(SeguroCasa.class))).thenReturn(seguroCasa);

        SeguroCasa result = seguroService.guardarSeguroCasa(seguro, "Calle Falsa 123", 250000.0, "Piso");

        assertNotNull(result);
        assertEquals("Calle Falsa 123", result.getDireccion());
        assertEquals(250000.0, result.getValorInmueble());
        assertEquals("Piso", result.getTipoVivienda());
        assertEquals(seguro, result.getSeguro());
        verify(seguroCasaRepository, times(1)).save(any(SeguroCasa.class));
    }
}
