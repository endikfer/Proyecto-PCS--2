package com.seguros.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import com.seguros.model.Seguro;

public class SeguroListCellRenderer extends JPanel implements ListCellRenderer<Seguro> {
    public final JLabel lblNombre;
    public final JLabel lblTipo;

    public SeguroListCellRenderer() {
        setLayout(new GridBagLayout());
        lblNombre = new JLabel();
        lblTipo = new JLabel();

        // Estilo para el nombre
        lblNombre.setFont(new Font("Arial", Font.BOLD, 18));

        // Estilo para el tipo (rectángulo con borde)
        lblTipo.setOpaque(true);
        lblTipo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTipo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 200), 1), // Borde externo
                BorderFactory.createEmptyBorder(2, 8, 2, 8) // Espaciado interno
        ));
        lblTipo.setPreferredSize(new Dimension(80, 20)); // Tamaño mínimo

        // Configuración del layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(2, 2, 2, 2); // Separación alrededor del nombre
        add(lblNombre, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new java.awt.Insets(2, 5, 2, 2); // Separación alrededor del tipo
        add(lblTipo, gbc);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Seguro> list, Seguro value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            lblNombre.setText(value.getNombre());
            lblTipo.setText(value.getTipoSeguro().toString());

            // Cambiar el color de fondo según el tipo de seguro
            switch (value.getTipoSeguro().toString().toLowerCase()) {
                case "vida" -> lblTipo.setBackground(new Color(208, 232, 255));
                case "coche" -> lblTipo.setBackground(new Color(223, 245, 216));
                case "casa" -> lblTipo.setBackground(new Color(255, 228, 217));
            }
        }

        // Cambiar colores si está seleccionado
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            lblNombre.setForeground(list.getSelectionForeground());
            lblTipo.setBackground(lblTipo.getBackground().darker());
            lblTipo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(list.getSelectionBackground().darker(), 1),
                    BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        } else {
            setBackground(list.getBackground());
            lblNombre.setForeground(list.getForeground());
            lblTipo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(150, 150, 200), 1),
                    BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        }

        setOpaque(true);
        return this;
    }
}