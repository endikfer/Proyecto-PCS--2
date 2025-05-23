package com.seguros.client;

import com.seguros.model.Seguro;
import javax.swing.*;
import java.awt.*;

public class SeguroVidaVentana {
    private JFrame frame;
    private JTextField txtEdad;
    private JTextField txtBeneficiarios;
    private Seguro seguro;
    SeguroControllerClient client = new SeguroControllerClient("localhost", "8080");

    public SeguroVidaVentana(Seguro seguro) {
        this.seguro = seguro;
        initialize();
    }

    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
    }

    void initialize() {
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
    String edadTexto = txtEdad.getText().trim();
    String beneficiarios = txtBeneficiarios.getText().trim();

    if (edadTexto.isEmpty() || beneficiarios.isEmpty()) {
        JOptionPane.showMessageDialog(frame,
                "Todos los campos son obligatorios.",
                "Campos incompletos",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        int edad = Integer.parseInt(edadTexto);
        if (edad <= 0) {
            throw new NumberFormatException("La edad debe ser un número positivo");
        }

        Long clienteId = 1L; // ⚠️ TODO: Obtener dinámicamente si es necesario
        Long seguroId = seguro.getId();

        String resultado = client.contratarSeguroVida(clienteId, seguroId, edad, beneficiarios);

        JOptionPane.showMessageDialog(frame,
                "Seguro contratado exitosamente:\n" + resultado,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(frame,
                "La edad debe ser un número válido.",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
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