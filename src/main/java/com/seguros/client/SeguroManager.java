package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SeguroManager {

    public static void main(String[] args) {
        // Crear el marco (ventana)
        JFrame ventana = new JFrame("Gestión de Seguros");

        // Configurar la ventana para que ocupe toda la pantalla
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza la ventana
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana

        // Opcional: Establecer un diseño o contenido
        ventana.setLayout(new BorderLayout());
        JLabel etiqueta = new JLabel("Bienvenido a la Gestión de Seguros", SwingConstants.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 24));
        ventana.add(etiqueta, BorderLayout.NORTH);

        // Crear un panel central para el texto y el campo de entrada
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

        // Añadir el panel central a la ventana
        ventana.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centra los botones
        JButton botonCrear = new JButton("CREAR");
        botonCrear.setPreferredSize(new Dimension(150, 50)); // Tamaño del botón (ancho, alto)
        botonCrear.setFont(new Font("Arial", Font.BOLD, 16));

        JButton botonEditar = new JButton("EDITAR");
        botonEditar.setPreferredSize(new Dimension(150, 50)); // Tamaño del botón (ancho, alto)
        botonEditar.setFont(new Font("Arial", Font.BOLD, 16));

        // Añadir ActionListener al botón CREAR
        botonCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón CREAR presionado");
                // Aquí puedes agregar la lógica para crear un seguro
            }
        });

        // Añadir ActionListener al botón EDITAR
        botonEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón EDITAR presionado");
                // Aquí puedes agregar la lógica para editar un seguro
            }
        });

        panelInferior.add(botonCrear);
        panelInferior.add(botonEditar);

        // Añadir el panel inferior a la ventana
        ventana.add(panelInferior, BorderLayout.SOUTH);

        // Hacer visible la ventana
        ventana.setVisible(true);
    }

}
