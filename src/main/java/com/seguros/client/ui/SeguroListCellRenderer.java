package com.seguros.client.ui;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import com.seguros.model.Seguro;

public class SeguroListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Seguro) {
            Seguro seguro = (Seguro) value;
            setText(seguro.getNombre() + " (" + seguro.getTipoSeguro() + ")");
        }
        return this;
    }
}
