package com.seguros.client;

import javax.swing.*;
import java.awt.*;

import com.seguros.model.Cliente;

public class PerfilVentana extends JFrame {

    public PerfilVentana(Cliente cliente) {
        setTitle("Perfil del Cliente");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Datos del Perfil", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombreLabel = new JLabel("Nombre: " + cliente.getNombre());
        nombreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email: " + cliente.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(20));
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(nombreLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(emailLabel);

        add(panel);
    }

    public void mostrar() {
        setVisible(true);
    }
}