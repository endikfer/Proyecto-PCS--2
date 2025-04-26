package com.seguros.client;

import java.awt.GraphicsEnvironment;
import org.junit.jupiter.api.Assumptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.seguros.model.Cliente;
import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;

public class SeguroManagerTest {

    @Mock
    private SeguroControllerClient seguroClientMock;

    private SeguroManager seguroManager;
    private Cliente clienteMock;

    @BeforeAll
    public static void setUp1() {
        // Verificar si el entorno es headless
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Entorno Headless detectado. Saltando test...");
            Assumptions.assumeTrue(false, "El entorno no soporta GUI");
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simular un cliente
        clienteMock = new Cliente();
        clienteMock.setEmail("test@example.com");

        // Crear una instancia de SeguroManager con el mock de SeguroControllerClient
        seguroManager = new SeguroManager();
        seguroManager.seguroClient = seguroClientMock;

        // Simular el comportamiento del cliente seguroClient
        when(seguroClientMock.obtenerSegurosPorTipo(anyString())).thenReturn(Collections.emptyList());
        when(seguroClientMock.obtenerTodosSeguros()).thenReturn(Collections.emptyList());

        // Configurar el email en inicioSesionVentana
        seguroManager.inicioSesionVentana.emailInicioSesion = "test@example.com";
    }

    @Test
    void testCrearVentanaPrincipal() {
        // Ejecutar el método
        seguroManager.crearVentanaPrincipal(clienteMock);

        // Verificar que los componentes de la ventana se configuran correctamente
        JLabel etiqueta = findComponentByType(JLabel.class);
        JButton btnPerfil = findComponentByType(JButton.class);

        assertNotNull(etiqueta, "La etiqueta de bienvenida debería estar presente");
        assertEquals("Bienvenido, test@example.com - Gestión de Seguros", etiqueta.getText());

        assertNotNull(btnPerfil, "El botón de perfil debería estar presente");
        assertEquals("Ver Perfil", btnPerfil.getText());

        // Verificar que se llamó al cliente seguroClient
        verify(seguroClientMock, times(1)).obtenerTodosSeguros();
    }

    @Test
    void testCrearPanelSeguros_UnSeguroNoVacio() {
        // Simular que el cliente devuelve una lista con un seguro cuyo nombre no es
        // "vacio"
        Seguro seguroNoVacio = new Seguro("Seguro Activo", "Descripción del seguro", TipoSeguro.CASA, 150.0);
        when(seguroClientMock.obtenerSegurosPorTipo("CASA")).thenReturn(Collections.singletonList(seguroNoVacio));

        // Crear una instancia de SeguroManager si el método no es estático
        JPanel panel = seguroManager.crearPanelSeguros(TipoSeguro.CASA);

        // Verificar que el panel no es nulo
        assertNotNull(panel, "El panel no debería ser nulo");

        // Verificar que contiene un JScrollPane con una JList
        assertEquals(1, panel.getComponentCount(), "El panel debería contener un componente");
        assertTrue(panel.getComponent(0) instanceof JScrollPane, "El componente debería ser un JScrollPane");
        JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
        JList<?> listaSeguros = (JList<?>) scrollPane.getViewport().getView();
        assertNotNull(listaSeguros, "La lista de seguros no debería ser nula");
        assertEquals(1, listaSeguros.getModel().getSize(), "La lista debería contener 1 seguro");

        // Verificar que el seguro en la lista es el esperado
        Seguro seguroEnLista = (Seguro) listaSeguros.getModel().getElementAt(0);
        assertEquals(seguroNoVacio, seguroEnLista, "El seguro no es el esperado");
    }

    @Test
    void testCrearPanelTodosSeguros_SeguroVacio() {
        // Simular que el cliente devuelve un seguro con nombre "vacio"
        Seguro seguroVacio = new Seguro("vacio", "Sin descripción", TipoSeguro.CASA,
                0.0);
        when(seguroClientMock.obtenerTodosSeguros()).thenReturn(Collections.singletonList(seguroVacio));

        // Ejecutar el método
        JPanel panel = seguroManager.crearPanelTodosSeguros();

        // Verificar que el panel no es nulo
        assertNotNull(panel, "El panel no debería ser nulo");

        // Verificar que contiene un JLabel con el mensaje esperado
        assertEquals(1, panel.getComponentCount(), "El panel debería contener un componente");
        assertTrue(panel.getComponent(0) instanceof JLabel, "El componente debería ser un JLabel");
        JLabel mensaje = (JLabel) panel.getComponent(0);
        assertEquals("No hay seguros de ningun tipo disponibles.", mensaje.getText(),
                "El mensaje no es el esperado");
    }

    @Test
    void testCrearPanelTodosSeguros_ListaValida() {
        // Simular que el cliente devuelve una lista de seguros válida
        Seguro seguro1 = new Seguro("Seguro Vida", "Cobertura completa",
                TipoSeguro.VIDA, 100.0);
        Seguro seguro2 = new Seguro("Seguro Casa", "Protección contra incendios",
                TipoSeguro.CASA, 200.0);
        Seguro seguro3 = new Seguro("Seguro Coche", "Cobertura de accidentes",
                TipoSeguro.COCHE, 300.0);
        when(seguroClientMock.obtenerTodosSeguros()).thenReturn(Arrays.asList(seguro1,
                seguro2, seguro3));

        // Ejecutar el método
        JPanel panel = seguroManager.crearPanelTodosSeguros();

        // Verificar que el panel no es nulo
        assertNotNull(panel, "El panel no debería ser nulo");

        // Verificar que contiene un JScrollPane con una JList
        assertEquals(1, panel.getComponentCount(), "El panel debería contener un componente");
        assertTrue(panel.getComponent(0) instanceof JScrollPane, "El componente debería ser un JScrollPane");
        JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
        JList<?> listaSeguros = (JList<?>) scrollPane.getViewport().getView();
        assertNotNull(listaSeguros, "La lista de seguros no debería ser nula");
        assertEquals(3, listaSeguros.getModel().getSize(), "La lista debería contener 3 seguros");
    }

    @Test
    void testCrearPanelTodosSeguros_UnSeguroNoVacio() {
        // Simular que el cliente devuelve una lista con un seguro cuyo nombre no es
        // "vacio"
        Seguro seguroNoVacio = new Seguro("Seguro Activo", "Descripción del seguro",
                TipoSeguro.CASA, 150.0);
        when(seguroClientMock.obtenerTodosSeguros()).thenReturn(Collections.singletonList(seguroNoVacio));

        // Ejecutar el método
        JPanel panel = seguroManager.crearPanelTodosSeguros();

        // Verificar que el panel no es nulo
        assertNotNull(panel, "El panel no debería ser nulo");

        // Verificar que contiene un JScrollPane con una JList
        assertEquals(1, panel.getComponentCount(), "El panel debería contener un componente");
        assertTrue(panel.getComponent(0) instanceof JScrollPane, "El componente debería ser un JScrollPane");
        JScrollPane scrollPane = (JScrollPane) panel.getComponent(0);
        JList<?> listaSeguros = (JList<?>) scrollPane.getViewport().getView();
        assertNotNull(listaSeguros, "La lista de seguros no debería ser nula");
        assertEquals(1, listaSeguros.getModel().getSize(), "La lista debería contener 1 seguro");

        // Comparar el seguro completo en lugar de solo el nombre
        Seguro seguroEnLista = (Seguro) listaSeguros.getModel().getElementAt(0);
        assertEquals(seguroNoVacio, seguroEnLista, "El seguro no es el esperado");
    }

    @Test
    void testCrearPanelTodosSeguros_ExceptionHandling() {
        // Simular una excepción al obtener los seguros
        when(seguroClientMock.obtenerTodosSeguros()).thenThrow(new RuntimeException("Simulated backend error"));

        // Ejecutar el método
        JPanel panel = seguroManager.crearPanelTodosSeguros();

        // Verificar que el panel no es nulo
        assertNotNull(panel, "El panel no debería ser nulo");

        // Nota: No se puede verificar directamente el JOptionPane, pero se puede
        // observar
        // que el método no lanza una excepción y el flujo continúa correctamente.
        verify(seguroClientMock, times(1)).obtenerTodosSeguros();
    }

    // @Test
    // void testAbrirVentanaSeguro_Coche() {
    // // Crear un seguro de tipo COCHE
    // Seguro seguroCoche = new Seguro("Seguro Coche", "Cobertura completa",
    // TipoSeguro.COCHE, 300.0);

    // // Mockear la ventana SeguroCocheVentana
    // SeguroCocheVentana ventanaMock = mock(SeguroCocheVentana.class);
    // doNothing().when(ventanaMock).mostrar();

    // // Ejecutar el método
    // SeguroManager.abrirVentanaSeguro(seguroCoche);

    // // Verificar que se llamó al método mostrar de SeguroCocheVentana
    // verify(ventanaMock, times(1)).mostrar();
    // }

    // @Test
    // void testAbrirVentanaSeguro_Vida() {
    // // Crear un seguro de tipo VIDA
    // Seguro seguroVida = new Seguro("Seguro Vida", "Cobertura de vida completa",
    // TipoSeguro.VIDA, 500.0);

    // // Mockear la ventana SeguroVidaVentana
    // SeguroVidaVentana ventanaMock = mock(SeguroVidaVentana.class);
    // doNothing().when(ventanaMock).mostrar();

    // // Ejecutar el método
    // SeguroManager.abrirVentanaSeguro(seguroVida);

    // // Verificar que se llamó al método mostrar de SeguroVidaVentana
    // verify(ventanaMock, times(1)).mostrar();
    // }

    // @Test
    // void testAbrirVentanaSeguro_Casa() {
    // // Crear un seguro de tipo CASA
    // Seguro seguroCasa = new Seguro("Seguro Casa", "Protección contra incendios",
    // TipoSeguro.CASA, 200.0);

    // // Mockear la ventana SeguroCasaVentana
    // SeguroCasaVentana ventanaMock = mock(SeguroCasaVentana.class);
    // doNothing().when(ventanaMock).mostrar();

    // // Ejecutar el método
    // SeguroManager.abrirVentanaSeguro(seguroCasa);

    // // Verificar que se llamó al método mostrar de SeguroCasaVentana
    // verify(ventanaMock, times(1)).mostrar();
    // }

    // @Test
    // void testAbrirVentanaSeguro_Default() {
    // // Crear un seguro con un tipo no reconocido (null)
    // Seguro seguroInvalido = new Seguro("Seguro Invalido", "Descripción no
    // válida", null, 0.0);

    // // Ejecutar el método
    // SeguroManager.abrirVentanaSeguro(seguroInvalido);

    // // Nota: No se puede verificar directamente el JOptionPane en este caso.
    // // Este test asegura que no se lanza una excepción y que el flujo continúa
    // // correctamente.
    // }

    private <T> T findComponentByType(Class<T> type) {
        // Buscar la ventana con el título específico
        for (java.awt.Window window : java.awt.Window.getWindows()) {
            if (window.isVisible() && window instanceof JFrame) {
                JFrame frame = (JFrame) window;
                if ("Gestión de Seguros".equals(frame.getTitle())) {
                    java.awt.Container container = frame.getContentPane();
                    return findComponentInContainer(container, type);
                }
            }
        }
        return null;
    }

    private <T> T findComponentInContainer(java.awt.Container container, Class<T> type) {
        for (java.awt.Component component : container.getComponents()) {
            if (type.isInstance(component)) {
                return type.cast(component);
            } else if (component instanceof java.awt.Container) {
                T result = findComponentInContainer((java.awt.Container) component, type);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
