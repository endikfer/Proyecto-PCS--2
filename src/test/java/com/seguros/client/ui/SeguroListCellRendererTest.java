package com.seguros.client.ui;

import static org.junit.jupiter.api.Assertions.*;

import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

class SeguroListCellRendererTest {

    private SeguroListCellRenderer renderer;
    private JList<Seguro> lista;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        renderer = new SeguroListCellRenderer();
        lista = new JList<>();
    }

    @Test
    void testRenderizarSeguro_ValoresCorrectos() {
        Seguro seguro = new Seguro("Seguro Vida", "Cobertura total", TipoSeguro.VIDA, 1000.0);
        Component component = renderer.getListCellRendererComponent(lista, seguro, 0, false, false);

        assertInstanceOf(JPanel.class, component);
        assertEquals("Seguro Vida", renderer.lblNombre.getText());
        assertEquals("VIDA", renderer.lblTipo.getText());
    }

    @Test
    void testRenderizarSeguro_ColorCorrectoSegunTipo() {
        Seguro seguroVida = new Seguro("Seguro Vida", "Cobertura total", TipoSeguro.VIDA, 1000.0);
        renderer.getListCellRendererComponent(lista, seguroVida, 0, false, false);
        assertEquals(new Color(208, 232, 255), renderer.lblTipo.getBackground());

        Seguro seguroCoche = new Seguro("Seguro Coche", "Cobertura total", TipoSeguro.COCHE, 800.0);
        renderer.getListCellRendererComponent(lista, seguroCoche, 0, false, false);
        assertEquals(new Color(223, 245, 216), renderer.lblTipo.getBackground());

        Seguro seguroHogar = new Seguro("Seguro Hogar", "Cobertura total", TipoSeguro.CASA, 1200.0);
        renderer.getListCellRendererComponent(lista, seguroHogar, 0, false, false);
        assertEquals(new Color(255, 228, 217), renderer.lblTipo.getBackground());
    }

    @Test
    void testRenderizarSeguro_Seleccionado() {
        Seguro seguro = new Seguro("Seguro Auto", "Cobertura total", TipoSeguro.COCHE, 800.0);
        Component component = renderer.getListCellRendererComponent(lista, seguro, 0, true, false);

        assertInstanceOf(JPanel.class, component);
        assertEquals(lista.getSelectionBackground(), renderer.getBackground());
        assertEquals(lista.getSelectionForeground(), renderer.lblNombre.getForeground());
    }

    @Test
    void testRenderizarSeguro_TipoSeguroCaseInsensitive() {
        Seguro seguroMayuscula = new Seguro("Seguro Salud", "Cobertura médica", TipoSeguro.valueOf("VIDA"), 1200.0);
        renderer.getListCellRendererComponent(lista, seguroMayuscula, 0, false, false);

        assertEquals(new Color(208, 232, 255), renderer.lblTipo.getBackground());

        Seguro seguroMinuscula = new Seguro("Seguro Salud", "Cobertura médica", TipoSeguro.valueOf("CASA"), 1200.0);
        renderer.getListCellRendererComponent(lista, seguroMinuscula, 0, false, false);

        assertEquals(new Color(255, 228, 217), renderer.lblTipo.getBackground());
    }

    /*
     * @Test
     * void testRenderizarSeguro_TipoSeguroNull() {
     * Seguro seguroNull = new Seguro("Seguro sin tipo", "Cobertura indefinida",
     * null, 900.0);
     * renderer.getListCellRendererComponent(lista, seguroNull, 0, false, false);
     * 
     * assertEquals(new Color(200, 200, 255), renderer.lblTipo.getBackground()); //
     * Debe caer en `default`
     * }
     */

    @Test
    void testRenderizarSeguro_Nulo() {
        Component component = renderer.getListCellRendererComponent(lista, null, 0, false, false);

        assertInstanceOf(JPanel.class, component);
        assertTrue(renderer.lblNombre.getText().isEmpty());
        assertTrue(renderer.lblTipo.getText().isEmpty());
    }
}
