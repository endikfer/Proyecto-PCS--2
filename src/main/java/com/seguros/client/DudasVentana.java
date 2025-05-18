package com.seguros.client;

import javax.swing.*;
import java.awt.*;

public class DudasVentana extends JFrame {

    private final SeguroControllerClient controller;

    public DudasVentana(SeguroControllerClient controller) {
        this.controller = controller;

        setTitle("Enviar una Duda");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Campo asunto
        JLabel labelAsunto = new JLabel("Asunto:");
        labelAsunto.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField campoAsunto = new JTextField();
        campoAsunto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        campoAsunto.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Campo mensaje de la duda
        JLabel labelDuda = new JLabel("Escribe tu duda:");
        labelDuda.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea areaDuda = new JTextArea(7, 20);
        areaDuda.setLineWrap(true);
        areaDuda.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaDuda);
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botón enviar
        JButton enviar = new JButton("Enviar Duda");
        enviar.addActionListener(e -> {
            String asunto = campoAsunto.getText().trim();
            String mensaje = areaDuda.getText().trim();

            if (asunto.isEmpty() || mensaje.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Por favor completa el asunto y la duda.",
                        "Campos incompletos",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                controller.enviarDuda(asunto, mensaje);
                JOptionPane.showMessageDialog(this,
                        "Tu duda ha sido enviada correctamente.",
                        "Duda Enviada",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al enviar la duda: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Añadir componentes al panel
        panel.add(labelAsunto);
        panel.add(campoAsunto);
        panel.add(Box.createVerticalStrut(10));
        panel.add(labelDuda);
        panel.add(scroll);
        panel.add(Box.createVerticalStrut(10));
        panel.add(enviar);

        add(panel);
    }

    public void mostrar() {
        setVisible(true);
    }
}