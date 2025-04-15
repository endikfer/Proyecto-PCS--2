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
        // Lógica para contratar el seguro de coche
        JOptionPane.showMessageDialog(frame, "Seguro de coche contratado exitosamente!");
        frame.dispose();
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}