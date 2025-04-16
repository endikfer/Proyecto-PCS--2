package com.seguros.client;

import com.seguros.model.Seguro;
import javax.swing.*;
import java.awt.*;

public class SeguroCocheVentana {
    private JFrame frame;
    private JTextField txtMatricula;
    private JTextField txtModelo;
    private JTextField txtMarca;
    private Seguro seguro;

    public SeguroCocheVentana(Seguro seguro) {
        this.seguro = seguro;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Seguro de Coche - " + seguro.getNombre());
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Matrícula:"));
        txtMatricula = new JTextField();
        panel.add(txtMatricula);

        panel.add(new JLabel("Modelo:"));
        txtModelo = new JTextField();
        panel.add(txtModelo);

        panel.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        panel.add(txtMarca);

        JButton btnContratar = new JButton("Contratar Seguro");
        btnContratar.addActionListener(e -> contratarSeguro());
        panel.add(btnContratar);

        frame.add(panel);
    }

    private void contratarSeguro() {
        String matricula = txtMatricula.getText().trim();
        String modelo = txtModelo.getText().trim();
        String marca = txtMarca.getText().trim();

        if (matricula.isEmpty() || modelo.isEmpty() || marca.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Todos los campos son obligatorios.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            JOptionPane.showMessageDialog(frame, 
                    "Seguro de coche contratado exitosamente!\n" +
                    "Seguro: " + seguro.getNombre() + "\n" +
                    "Matrícula: " + matricula + "\n" +
                    "Modelo: " + modelo + "\n" +
                    "Marca: " + marca);
            frame.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error al contratar el seguro: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}