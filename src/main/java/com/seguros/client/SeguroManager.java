package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SeguroManager {

    private static final String hostname;
    private static final String port;
    private static final SeguroControllerClient client;
    private static final InicioSesionVentana inicioSesionVentana;

    static {
        hostname = System.getProperty("hostname", "localhost");
        port = System.getProperty("port", "8080");
        client = new SeguroControllerClient(hostname, port);
        inicioSesionVentana = new InicioSesionVentana();
    }

    public static void main(String[] args) {
        inicioSesionVentana.mostrar();
    }

    public static void crearVentanaPrincipal() {
        SeguroVentana ventanaCrear = new SeguroVentana();
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

        JLabel labelSeguro = new JLabel("Seguro a modificar:");
        labelSeguro.setFont(new Font("Arial", Font.PLAIN, 18));
        panelCentral.add(labelSeguro, gbc);

        gbc.gridx = 1;
        JTextField textFieldSeguro = new JTextField();
        textFieldSeguro.setPreferredSize(new Dimension(300, 30));
        panelCentral.add(textFieldSeguro, gbc);

        ventana.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botonCrear = new JButton("CREAR");
        botonCrear.setPreferredSize(new Dimension(150, 50));
        botonCrear.setFont(new Font("Arial", Font.BOLD, 16));

        JButton botonEditar = new JButton("EDITAR");
        botonEditar.setPreferredSize(new Dimension(150, 50));
        botonEditar.setFont(new Font("Arial", Font.BOLD, 16));

        JButton botonCerrarSesion = new JButton("CERRAR SESIÓN");
        botonCerrarSesion.setPreferredSize(new Dimension(200, 50));
        botonCerrarSesion.setFont(new Font("Arial", Font.BOLD, 16));

        botonCrear.addActionListener(e -> ventanaCrear.crearVentanaSeguro(false));

        botonEditar.addActionListener(e -> ventanaCrear.editarSeguro(textFieldSeguro.getText()));

        botonCerrarSesion.addActionListener(e -> llamarLogoutClienteAPI(ventana));

        panelInferior.add(botonCrear);
        panelInferior.add(botonEditar);
        panelInferior.add(botonCerrarSesion);

        ventana.add(panelInferior, BorderLayout.SOUTH);
        ventana.setVisible(true);
    }

    private static void llamarLogoutClienteAPI(JFrame ventana) {
        try {
            String urlCompleta = "http://" + hostname + ":" + port + "/api/clientes/logout";
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