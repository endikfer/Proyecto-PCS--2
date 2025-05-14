package com.seguros.client;

import javax.swing.JFrame;

public class FacturaVenatana {
    public void mostrar() {
        JFrame ventana = new JFrame("Factura");
        ventana.setSize(400, 300); // Tamaño de la ventana
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerrar solo esta ventana
        ventana.setVisible(true); // Mostrar la ventana
    }
}
