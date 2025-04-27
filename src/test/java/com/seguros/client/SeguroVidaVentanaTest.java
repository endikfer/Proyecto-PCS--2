package com.seguros.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.seguros.model.Seguro;

import javax.swing.*;

@ExtendWith(MockitoExtension.class)
public class SeguroVidaVentanaTest {

    @BeforeAll
    public static void setUp1() {
        // Leer la propiedad para determinar si el entorno es headless
        String isHeadlessProperty = System.getProperty("java.awt.headless", "false");
        boolean isHeadless = Boolean.parseBoolean(isHeadlessProperty);
        Assumptions.assumeTrue(!isHeadless, "El entorno es headless. Saltando pruebas de GUI.");
    }

    @Mock
    private AdminVentana adminVentanaMock;

    private SeguroVentana seguroVentana;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        seguroVentana = new SeguroVentana(adminVentanaMock);
    }

    @Test
    void testSustituirEspacios() {
        String original = "Seguro de Vida Familiar";
        String esperado = "Seguro;de;Vida;Familiar";
        String resultado = SeguroVentana.sustituirEspacios(original);
        assertEquals(esperado, resultado);
    }

    @Test
    void testCrearVentanaModoGuardar() {
        seguroVentana.crearVentanaSeguro(1);
        assertNotNull(seguroVentana.btnGuardar);
        assertEquals("Guardar", seguroVentana.btnGuardar.getText());
    }

    @Test
    void testCrearVentanaModoSalir() {
        seguroVentana.crearVentanaSeguro(2);
        assertNotNull(seguroVentana.btnGuardar);
        assertEquals("Salir", seguroVentana.btnGuardar.getText());
    }

    @Test
    void testGuardarSeguroCamposVacios() {
        seguroVentana.crearVentanaSeguro(1);

        setTextField("txtNombre", "");
        setTextAreaField("txtDescripcion", "");
        setTextField("txtPrecio", "");

        JButton guardar = seguroVentana.btnGuardar;
        guardar.doClick(); // Simula click
    }

    @Test
    void testGuardarSeguroDatosInvalidos() {
        seguroVentana.crearVentanaSeguro(1);

        setTextField("txtNombre", "Seguro Test");
        setTextAreaField("txtDescripcion", "Descripción Test");
        setTextField("txtPrecio", "no_es_numero"); // Precio inválido

        JButton guardar = seguroVentana.btnGuardar;
        guardar.doClick(); // Simula click
    }

    @Test
    void testGuardarSeguroDatosCorrectos() {
        seguroVentana.crearVentanaSeguro(1);

        String nombreSeguroUnico = "SeguroTest_" + System.currentTimeMillis();

        setTextField("txtNombre", nombreSeguroUnico);
        setTextAreaField("txtDescripcion", "Descripción Test");
        setTextField("txtPrecio", "99.99");

        JButton guardar = seguroVentana.btnGuardar;
        guardar.doClick();
    }

    @Test
    void testEditarSeguroNombreVacio() {
        seguroVentana.editarSeguro(""); // Nombre vacío
    }

    @Test
    void testEliminarSeguroNombreVacio() {
        seguroVentana.eliminarSeguro(""); // Nombre vacío
    }

    @Test
    void testInformacionSeguroNombreVacio() {
        seguroVentana.informacionSeguro(""); // Nombre vacío
    }

    // Métodos auxiliares para setear campos privados

    private void setTextField(String fieldName, String value) {
        try {
            var field = SeguroVentana.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            JTextField txt = (JTextField) field.get(seguroVentana);
            txt.setText(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setTextAreaField(String fieldName, String value) {
        try {
            var field = SeguroVentana.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            JTextArea txt = (JTextArea) field.get(seguroVentana);
            txt.setText(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
