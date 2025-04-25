package com.seguros.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.seguros.model.Cliente;

public class SeguroManagerTest {

    @Mock
    private SeguroControllerClient seguroClientMock;

    private Cliente clienteMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Simular un cliente
        clienteMock = new Cliente();
        clienteMock.setEmail("test@example.com");

        // Simular el comportamiento del cliente seguroClient
        when(seguroClientMock.obtenerSegurosPorTipo(anyString())).thenReturn(Collections.emptyList());
        when(seguroClientMock.obtenerTodosSeguros()).thenReturn(Collections.emptyList());

        // Reemplazar el cliente seguroClient en SeguroManager
        SeguroManager.seguroClient = seguroClientMock;

        // Configurar el email en inicioSesionVentana
        SeguroManager.inicioSesionVentana.emailInicioSesion = "test@example.com";
    }

    @Test
    void testCrearVentanaPrincipal() {
        // Ejecutar el método
        SeguroManager.crearVentanaPrincipal(clienteMock);

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
