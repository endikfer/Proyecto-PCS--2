package com.seguros.client;

import com.seguros.model.Seguro;
import javax.swing.*;
import java.awt.*;

public class SeguroVidaVentana {
    private JFrame frame;
    private JTextField txtEdad;
    private JTextField txtBeneficiarios;
    private Seguro seguro;

    public SeguroVidaVentana(Seguro seguro) {
        this.seguro = seguro;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Seguro de Vida - " + seguro.getNombre());
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Edad del Asegurado:"));
        txtEdad = new JTextField();
        panel.add(txtEdad);

        panel.add(new JLabel("Beneficiarios:"));
        txtBeneficiarios = new JTextField();
        panel.add(txtBeneficiarios);

        JButton btnContratar = new JButton("Contratar Seguro");
        btnContratar.addActionListener(e -> contratarSeguro());
        panel.add(btnContratar);

        frame.add(panel);
    }

    private void contratarSeguro() {
        // Lógica para contratar el seguro de vida
        JOptionPane.showMessageDialog(frame, "Seguro de vida contratado exitosamente!");
        frame.dispose();
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}