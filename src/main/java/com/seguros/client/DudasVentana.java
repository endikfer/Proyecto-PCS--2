package com.seguros.client;

import javax.swing.*;
import java.awt.*;

public class DudasVentana extends JFrame {
    private final SeguroControllerClient controller;
    private final String email;
    
    public DudasVentana(SeguroControllerClient controller, String email) {
        this.controller = null;
        this.email = "";
        // Configurar la ventana principal
        setTitle("Enviar una Duda");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear panel principal
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Crear área de texto para escribir la duda
        JTextArea areaDuda = new JTextArea();
        areaDuda.setLineWrap(true);
        areaDuda.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaDuda);

        // Crear botón para enviar la duda
        JButton enviar = new JButton("Enviar Duda");
        enviar.addActionListener(e -> {
            String duda = areaDuda.getText().trim();
            if (!duda.isEmpty()) {
                try {
                    controller.enviarDuda(email, duda);  // Llamada a la API REST
                    JOptionPane.showMessageDialog(this,
                            "Tu duda ha sido enviada correctamente.",
                            "Duda Enviada",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Cerrar la ventana
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al enviar la duda: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Por favor escribe tu duda antes de enviarla.",
                        "Campo vacío",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // Añadir componentes al panel
        panel.add(new JLabel("Escribe tu duda:"), BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(enviar, BorderLayout.SOUTH);

        // Añadir panel a la ventana
        add(panel);
    }

    // Método para mostrar la ventana
    public void mostrar() {
        setVisible(true);
    }
}