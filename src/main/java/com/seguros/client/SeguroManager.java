package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.seguros.model.Cliente;

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

    // Ahora recibe el cliente logueado
    public static void crearVentanaPrincipal(Cliente cliente) {
        JFrame ventana = new JFrame("Gestión de Seguros");
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new FlowLayout());

        JLabel bienvenido = new JLabel("Bienvenido al sistema de gestión de seguros");
        bienvenido.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(bienvenido);

        // Botón de perfil
        JButton btnPerfil = new JButton("Ver Perfil");
        btnPerfil.addActionListener(e -> {
            new PerfilVentana(cliente);
        });
        panel.add(btnPerfil);

        ventana.add(panel, BorderLayout.CENTER);
        ventana.setVisible(true);
    }
}