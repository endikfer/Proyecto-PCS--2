package com.seguros.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdminVentana {
    private JFrame frame;

    public AdminVentana() {
        frame = new JFrame("Panel de Administrador");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Bienvenido, Administrador", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // Botón Cerrar Sesión
        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(btnCerrarSesion, gbc);

        btnCerrarSesion.addActionListener((ActionEvent e) -> {
            llamarLogoutAPI();
        });

        frame.add(panel);
    }

    private void llamarLogoutAPI() {
        SwingUtilities.invokeLater(() -> {
            try {
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
                ex.printStackTrace(); // Esto imprime en consola detalles del error
            }
        });
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    // Para probar directamente esta ventana:
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminVentana().mostrar();
        });
    }
}