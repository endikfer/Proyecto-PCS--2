package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;

public class AdminVentanaTest {

    private AdminVentana adminVentana;
    private AdminVentana adminVentana1;

    @Mock
    private JPanel mockPanelCentral;

    @Mock
    private JPanel mockPanelSuperiorCentral;

    @Mock
    private SeguroControllerAdmin mockSeguroControllerAdmin;

    @BeforeAll
    public static void setUp1() {
        // Verificar si el entorno es headless
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Entorno Headless detectado. Saltando test...");
            Assumptions.assumeTrue(false, "El entorno no soporta GUI");
        }
    }

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);
        adminVentana = new AdminVentana(mockSeguroControllerAdmin);
        adminVentana1 = new AdminVentana();

        // Reemplazar los paneles reales por mocks
        adminVentana.panelCentral = mockPanelCentral;
        adminVentana.panelSuperiorCentral = mockPanelSuperiorCentral;
        adminVentana.modeloLista = new DefaultListModel<>();
    }

    @Test
    public void testAdminVentanaConstructor() {
        // Reemplazar los paneles reales por los mocks
        adminVentana.panelCentral = mockPanelCentral;
        adminVentana.panelSuperiorCentral = mockPanelSuperiorCentral;

        // Verify JFrame properties
        assertNotNull(adminVentana, "AdminVentana instance should not be null");
        assertNotNull(adminVentana.frame, "Frame should be initialized");
        assertEquals("Panel de Administrador", adminVentana.frame.getTitle(), "Frame title should match");
        assertEquals(javax.swing.JFrame.MAXIMIZED_BOTH,
                adminVentana.frame.getExtendedState(),
                "Frame should be maximized");
        assertEquals(javax.swing.JFrame.EXIT_ON_CLOSE,
                adminVentana.frame.getDefaultCloseOperation(),
                "Default close operation should be EXIT_ON_CLOSE");

        // Verify panelCentral and panelSuperiorCentral are initialized
        assertNotNull(adminVentana.panelCentral, "Panel central should be initialized");
        assertNotNull(adminVentana.panelSuperiorCentral, "Panel superior central should be initialized");

        // Verify left panel buttons
        JPanel panelIzquierdo = (JPanel) adminVentana.frame.getContentPane().getComponent(0);
        assertNotNull(panelIzquierdo, "Left panel should be added to the frame");
        assertEquals(5, panelIzquierdo.getComponentCount(), "Left panel should contain 5 buttons");

        // Verify button names
        String[] expectedButtonNames = { "Seguros", "Seguros cliente", "Clientes por seguro", "Clientes", "Dudas" };
        for (int i = 0; i < expectedButtonNames.length; i++) {
            JButton button = (JButton) panelIzquierdo.getComponent(i);
            assertEquals(expectedButtonNames[i], button.getText(), "Button text should match");
        }
    }

    @Test
    public void testCambiarContenido_opcion1() {
        // Ejecutar el método con la opción 1
        adminVentana.cambiarContenido(1);

        // Verificar que el panel central se limpia y se actualiza
        verify(mockPanelCentral).removeAll();
        verify(mockPanelCentral, times(2)).add(mockPanelSuperiorCentral,
                BorderLayout.NORTH); // Verificar dos llamadas
        verify(mockPanelCentral).revalidate();
        verify(mockPanelCentral).repaint();
    }

    // @Test
    // public void testCambiarContenido_opcion2() {
    // Ejecutar el método con la opción 2
    // adminVentana.cambiarContenido(2);

    // Verificar que el panel central se limpia y se actualiza
    // verify(mockPanelCentral).removeAll();
    // verify(mockPanelCentral).add(mockPanelSuperiorCentral,
    // BorderLayout.NORTH);
    // verify(mockPanelCentral).revalidate();
    // verify(mockPanelCentral).repaint();
    // }

    // @Test
    // public void testCambiarContenido_opcion3() {
    // // Ejecutar el método con la opción 3
    // adminVentana.cambiarContenido(3);

    // // Verificar que el panel central se limpia y se actualiza
    // verify(mockPanelCentral).removeAll();
    // verify(mockPanelCentral).add(mockPanelSuperiorCentral, BorderLayout.NORTH);
    // verify(mockPanelCentral).revalidate();
    // verify(mockPanelCentral).repaint();
    // }

    @Test
    public void testCambiarContenido_opcion4() {
        // Ejecutar el método con la opción 4
        adminVentana.cambiarContenido(4);

        // Verificar que el panel central se limpia y se actualiza
        verify(mockPanelCentral).removeAll();
        verify(mockPanelCentral).add(mockPanelSuperiorCentral, BorderLayout.NORTH);
        verify(mockPanelCentral).revalidate();
        verify(mockPanelCentral).repaint();
    }

    @Test
    public void testCambiarContenido_opcion5() {
        // Ejecutar el método con la opción 5
        adminVentana.cambiarContenido(5);

        // Verificar que el panel central se limpia y se actualiza
        verify(mockPanelCentral).removeAll();
        verify(mockPanelCentral).add(mockPanelSuperiorCentral, BorderLayout.NORTH);
        verify(mockPanelCentral).revalidate();
        verify(mockPanelCentral).repaint();
    }

    @Test
    public void testCambiarContenido_opcionInvalida() {
        // Ejecutar el método con una opción inválida
        adminVentana.cambiarContenido(99);

        // Verificar que el panel central se limpia y se actualiza
        verify(mockPanelCentral).removeAll();
        verify(mockPanelCentral).add(mockPanelSuperiorCentral, BorderLayout.NORTH);
        verify(mockPanelCentral).revalidate();
        verify(mockPanelCentral).repaint();
    }

    @Test
    public void testActualizarListaSeguros_conSeguros() {
        // Simular una lista de seguros
        List<Seguro> seguros = Arrays.asList(
                new Seguro("Seguro Vida", "Cobertura completa de vida", TipoSeguro.VIDA,
                        100.0),
                new Seguro("Seguro Casa", "Cobertura para el hogar", TipoSeguro.CASA, 200.0),
                new Seguro("Seguro Coche", "Cobertura para vehículos", TipoSeguro.COCHE,
                        300.0));

        // Simular el comportamiento del controlador
        when(mockSeguroControllerAdmin.listaNombreSeguros()).thenReturn(seguros);

        // Ejecutar el método
        adminVentana.actualizarListaSeguros();

        // Verificar que el modelo de la lista contiene los nombres de los seguros
        assertEquals(3, adminVentana.modeloLista.size());
        assertEquals("Seguro Vida", adminVentana.modeloLista.get(0));
        assertEquals("Seguro Casa", adminVentana.modeloLista.get(1));
        assertEquals("Seguro Coche", adminVentana.modeloLista.get(2));
    }

    @Test
    public void testActualizarListaSeguros_listaNula() {
        // Simular un caso donde la lista de seguros es null
        when(mockSeguroControllerAdmin.listaNombreSeguros()).thenReturn(null);

        // Ejecutar el método
        adminVentana.actualizarListaSeguros();

        // Verificar que el modelo de la lista está vacío
        assertEquals(0, adminVentana.modeloLista.size());
    }

    @Test
    public void testActualizarListaSeguros_sinSeguros() {
        // Simular una lista vacía
        when(mockSeguroControllerAdmin.listaNombreSeguros()).thenReturn(Collections.emptyList());

        // Ejecutar el método
        adminVentana.actualizarListaSeguros();

        // Verificar que el modelo de la lista está vacío
        assertEquals(0, adminVentana.modeloLista.size());
    }

    @Test
    public void testActualizarListaSeguros_conSeguroVacio() {
        // Simular una lista con un seguro "vacio"
        List<Seguro> seguros = Collections.singletonList(new Seguro("vacio", "Sin descripción", TipoSeguro.VIDA, 0.0));

        // Simular el comportamiento del controlador
        when(mockSeguroControllerAdmin.listaNombreSeguros()).thenReturn(seguros);

        // Ejecutar el método
        adminVentana.actualizarListaSeguros();

        // Verificar que el modelo de la lista contiene el mensaje "No hay seguros
        // creados"
        assertEquals(1, adminVentana.modeloLista.size());
        assertEquals("No hay seguros creados", adminVentana.modeloLista.get(0));
    }

    @Test
    public void testActualizarListaSeguros_conUnSeguroNoVacio() {
        // Simular una lista con un solo seguro cuyo nombre no es "vacio"
        List<Seguro> seguros = Collections
                .singletonList(new Seguro("Seguro Único", "Descripción única",
                        TipoSeguro.VIDA, 100.0));

        // Simular el comportamiento del controlador
        when(mockSeguroControllerAdmin.listaNombreSeguros()).thenReturn(seguros);

        // Ejecutar el método
        adminVentana.actualizarListaSeguros();

        // Verificar que el modelo de la lista contiene el nombre del seguro
        assertEquals(1, adminVentana.modeloLista.size());
        assertEquals("Seguro Único", adminVentana.modeloLista.get(0));
    }

    @Test
    public void testActualizarListaSeguros_conExcepcion() {
        // Simular una excepción al obtener los seguros
        SeguroControllerAdmin seguroControllerAdmin = mock(SeguroControllerAdmin.class);
        when(seguroControllerAdmin.listaNombreSeguros()).thenThrow(new RuntimeException("Error de conexión"));

        // Ejecutar el método
        adminVentana.actualizarListaSeguros();

        // Verificar que el modelo de la lista está vacío
        assertEquals(0, adminVentana.modeloLista.size());
    }

    @Test
    public void testMostrar() {
        // Verificar que el frame no es visible inicialmente
        assertFalse(adminVentana1.frame.isVisible(), "El frame debería estar inicialmente invisible");

        // Llamar al método mostrar
        adminVentana1.mostrar();

        // Verificar que el frame ahora es visible
        assertTrue(adminVentana1.frame.isVisible(), "El frame debería ser visible después de llamar a mostrar");
    }

}