package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class AdminVentana {
    private final JFrame frame;
    private final JPanel panelCentral;

    public AdminVentana() {
        frame = new JFrame("Panel de Administrador");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Panel superior con el botón "Cerrar sesión"
        JPanel panelSuperior = new JPanel(new BorderLayout());
        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.addActionListener((ActionEvent e) -> llamarLogoutAPI());
        panelSuperior.add(btnCerrarSesion, BorderLayout.EAST); // Botón alineado a la derecha

        // Panel izquierdo con botones
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH; // Los botones ocuparán todo el espacio disponible
        gbc.weightx = 1.0; // Expandir horizontalmente
        gbc.weighty = 1.0; // Expandir verticalmente

        // Crear botones
        String[] nombresBotones = { "Seguros", "Seguros cliente", "Clientes por seguro", "Clientes", "Dudas" };
        for (int i = 0; i < nombresBotones.length; i++) {
            JButton boton = new JButton(nombresBotones[i]);
            gbc.gridy = i; // Posición vertical
            gbc.gridx = 0;
            panelIzquierdo.add(boton, gbc);

            // Agregar acción al botón
            int opcion = i + 1;
            boton.addActionListener((ActionEvent e) -> cambiarContenido(opcion));
        }

        // Panel central inicial
        panelCentral = new JPanel(new GridBagLayout());
        mostrarContenidoSeguros(); // Mostrar contenido inicial (primer botón)

        // Agregar paneles al frame
        frame.add(panelSuperior, BorderLayout.NORTH); // Panel superior fijo
        frame.add(panelIzquierdo, BorderLayout.WEST); // Panel izquierdo fijo
        frame.add(panelCentral, BorderLayout.CENTER); // Panel central dinámico
    }

    private void llamarLogoutAPI() {
        SwingUtilities.invokeLater(() -> {
            try {
                @SuppressWarnings("deprecation")
                URL url = new URL("http://localhost:8080/api/admin/logout");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true); // Muy importante para una petición POST

                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    JOptionPane.showMessageDialog(frame, "Sesión cerrada correctamente.");
                    frame.dispose();
                    new InicioSesionVentana().mostrar();
                } else {
                    JOptionPane.showMessageDialog(frame, "Error al cerrar sesión. Código: " + responseCode,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

                connection.disconnect();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error de conexión: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cambiarContenido(int opcion) {
        // Limpiar el panel central
        panelCentral.removeAll();

        // Llamar al método correspondiente según la opción seleccionada
        switch (opcion) {
            case 1 -> mostrarContenidoSeguros();
            case 2 -> mostrarContenidoSegurosCliente();
            case 3 -> mostrarContenidoClientesPorSeguro();
            case 4 -> mostrarContenidoClientes();
            case 5 -> mostrarContenidoDudas();
            default -> mostrarContenidoInvalido();
        }

        // Actualizar la vista
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private void mostrarContenidoSeguros() {
        JLabel lblSeguros = new JLabel("Contenido de la opción 1: Seguros", SwingConstants.CENTER);
        lblSeguros.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblSeguros);
    }

    private void mostrarContenidoSegurosCliente() {
        JLabel lblSegurosCliente = new JLabel("Contenido de la opción 2: Seguros cliente", SwingConstants.CENTER);
        lblSegurosCliente.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblSegurosCliente);
    }

    private void mostrarContenidoClientesPorSeguro() {
        JLabel lblClientesPorSeguro = new JLabel("Contenido de la opción 3: Clientes por seguro",
                SwingConstants.CENTER);
        lblClientesPorSeguro.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblClientesPorSeguro);
    }

    private void mostrarContenidoClientes() {
        JLabel lblClientes = new JLabel("Contenido de la opción 4: Clientes", SwingConstants.CENTER);
        lblClientes.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblClientes);
    }

    private void mostrarContenidoDudas() {
        JLabel lblDudas = new JLabel("Contenido de la opción 5: Dudas", SwingConstants.CENTER);
        lblDudas.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblDudas);
    }

    private void mostrarContenidoInvalido() {
        JLabel lblInvalido = new JLabel("Opción no válida", SwingConstants.CENTER);
        lblInvalido.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblInvalido);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}