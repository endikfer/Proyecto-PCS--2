package com.seguros.client;

import com.seguros.model.Seguro;
import com.seguros.model.SeguroCasa;

import javax.swing.*;
import java.awt.*;

public class SeguroCasaVentana {
    private JFrame frame;
    private JTextField txtDireccion;
    private JTextField txtValor;
    private JTextField txtTipoVivienda;
    private Seguro seguro;

    public SeguroCasaVentana(Seguro seguro) {
        this.seguro = seguro;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Seguro de Casa - " + seguro.getNombre());
        frame.setSize(450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panel.add(txtDireccion);

        panel.add(new JLabel("Valor del Inmueble:"));
        txtValor = new JTextField();
        panel.add(txtValor);

        panel.add(new JLabel("Tipo de Vivienda:"));
        txtTipoVivienda = new JTextField();
        panel.add(txtTipoVivienda);

        JButton btnContratar = new JButton("Contratar Seguro");
        btnContratar.addActionListener(e -> contratarSeguro());
        panel.add(new JLabel());
        panel.add(btnContratar);

        frame.add(panel);
    }

    private void contratarSeguro() {
        String direccion = txtDireccion.getText().trim();
        String valorTexto = txtValor.getText().trim();
        String tipoVivienda = txtTipoVivienda.getText().trim();

        if (direccion.isEmpty() || valorTexto.isEmpty() || tipoVivienda.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Todos los campos son obligatorios.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        double valorInmueble;
        try {
            valorInmueble = Double.parseDouble(valorTexto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame,
                    "El valor del inmueble debe ser un número válido.",
                    "Valor inválido",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        SeguroCasa seguroCasa = new SeguroCasa(seguro, direccion, valorInmueble, tipoVivienda);

        JOptionPane.showMessageDialog(frame,
                "Seguro de casa contratado exitosamente:\n" + seguroCasa.toString(),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}
