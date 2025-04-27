package com.seguros.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

public class SeguroVentanaTest {

    private SeguroVentana seguroVentana;
    private SeguroControllerAdmin adminMock;

    @BeforeAll
    public static void setUp1() {
        // Leer la propiedad para determinar si el entorno es headless
        String isHeadlessProperty = System.getProperty("java.awt.headless", "false");
        boolean isHeadless = Boolean.parseBoolean(isHeadlessProperty);
        Assumptions.assumeTrue(!isHeadless, "El entorno es headless. Saltando pruebas de GUI.");
    }

    @BeforeEach
    public void setUp() {
        adminMock = mock(SeguroControllerAdmin.class);
        SeguroVentana.setAdmin(adminMock);
        seguroVentana = new SeguroVentana();
    }

    @Test
    public void testSustituirEspacios() {
        String original = "Seguro de Vida";
        String esperado = "Seguro;de;Vida";
        assertEquals(esperado, SeguroVentana.sustituirEspacios(original));
    }

    @Test
    public void testEditarSeguro_CargaDatosCorrectamente() {
        // Arrange
        Seguro seguroMock = new Seguro("Seguro de Hogar", "Protección total", TipoSeguro.CASA, 150.0);
        seguroMock.setId(1L); // <-- Agregado
        when(adminMock.obtenerSeguroPorNombre("Seguro;de;Hogar")).thenReturn(seguroMock);

        // Act
        seguroVentana.editarSeguro("Seguro de Hogar");

        // Assert
        assertEquals("Seguro de Hogar", seguroVentana.getTxtNombre().getText());
        assertEquals("Protección total", seguroVentana.getTxtDescripcion().getText());
        assertEquals(TipoSeguro.CASA, seguroVentana.getComboTipoSeguro().getSelectedItem());
        assertEquals("150.0", seguroVentana.getTxtPrecio().getText());
    }

    @Test
    public void testEliminarSeguro_SeguroEncontradoYEliminado() {
        // Arrange
        Seguro seguroMock = new Seguro("Seguro de Auto", "Cobertura total", TipoSeguro.COCHE, 200.0);
        seguroMock.setId(1L); // <-- Agregado
        when(adminMock.obtenerSeguroPorNombre("Seguro;de;Auto")).thenReturn(seguroMock);

        // Simular que el usuario presiona "Sí" (YES_OPTION)
        JOptionPane pane = new JOptionPane();
        JDialog dialog = pane.createDialog("Confirmar eliminación");
        SwingUtilities.invokeLater(() -> pane.setValue(JOptionPane.YES_OPTION));

        // Act
        seguroVentana.eliminarSeguro("Seguro de Auto");

        // Assert
        verify(adminMock).eliminarSeguro(1L); // Ahora sí eliminará el 1L
    }

    @Test
    public void testInformacionSeguro_CargaDatosEnModoLectura() {
        // Arrange
        Seguro seguroMock = new Seguro();
        seguroMock.setId(1L);
        seguroMock.setNombre("Seguro Médico");
        seguroMock.setDescripcion("Cobertura médica completa"); // <-- Faltaba
        seguroMock.setTipoSeguro(TipoSeguro.VIDA); // <-- Faltaba
        seguroMock.setPrecio(300.0); // <-- Faltaba
        when(adminMock.obtenerSeguroPorNombre("Seguro;Médico")).thenReturn(seguroMock);

        // Act
        seguroVentana.informacionSeguro("Seguro Médico");

        // Assert
        assertEquals("Seguro Médico", seguroVentana.getTxtNombre().getText());
        assertEquals("Cobertura médica completa", seguroVentana.getTxtDescripcion().getText());
        assertEquals(TipoSeguro.VIDA, seguroVentana.getComboTipoSeguro().getSelectedItem());
        assertEquals("300.0", seguroVentana.getTxtPrecio().getText());

        assertFalse(seguroVentana.getTxtNombre().isEditable());
        assertFalse(seguroVentana.getTxtDescripcion().isEditable());
        assertFalse(seguroVentana.getTxtPrecio().isEditable());
        assertFalse(seguroVentana.getComboTipoSeguro().isEnabled());
    }
}
