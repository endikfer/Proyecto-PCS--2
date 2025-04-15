package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SeguroManager {

    private static final String HOSTNAME;
    private static final String PORT;
    private static final InicioSesionVentana inicioSesionVentana;

    static {
        HOSTNAME = System.getProperty("hostname", "localhost");
        PORT = System.getProperty("port", "8080");
        inicioSesionVentana = new InicioSesionVentana();
    }

    public static void main(String[] args) {
        inicioSesionVentana.mostrar();
    }

    public static void crearVentanaPrincipal() {
        JFrame ventana = new JFrame("Gestión de Seguros");
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new BorderLayout());

        // Mostrar email del cliente logueado
        String email = inicioSesionVentana.emailInicioSesion;
        JLabel etiqueta = new JLabel("Bienvenido, " + email + " - Gestión de Seguros", SwingConstants.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 24));
        ventana.add(etiqueta, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets.set(10, 10, 10, 10);

        ventana.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botonCerrarSesion = new JButton("CERRAR SESIÓN");
        botonCerrarSesion.setPreferredSize(new Dimension(200, 50));
        botonCerrarSesion.setFont(new Font("Arial", Font.BOLD, 16));

        botonCerrarSesion.addActionListener(e -> llamarLogoutClienteAPI(ventana));
        panelInferior.add(botonCerrarSesion);

        ventana.add(panelInferior, BorderLayout.SOUTH);
        ventana.setVisible(true);
    }

    private static void llamarLogoutClienteAPI(JFrame ventana) {
        try {
            String urlCompleta = "http://" + HOSTNAME + ":" + PORT + "/api/clientes/logout";
            @SuppressWarnings("deprecation")
            java.net.URL url = new java.net.URL(urlCompleta);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(null, "Sesión cerrada correctamente.");
                ventana.dispose();
                inicioSesionVentana.mostrar();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error al cerrar sesión. Código: " + responseCode,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la API: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}