package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.seguros.model.Factura;

public class FacturaVenatana {

    private final String HOSTNAME = System.getProperty("hostname", "localhost");
    private final String PORT = System.getProperty("port", "8080");

    public FacturaControllerClient FacturaContr = new FacturaControllerClient(HOSTNAME, PORT);
    public void mostrar(String gmail) {
        JFrame ventana = new JFrame("Facturas del Cliente");
        ventana.setSize(500, 400);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setLayout(new BorderLayout());

        // Título en la parte superior
        JLabel titulo = new JLabel("Listado de Facturas", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        ventana.add(titulo, BorderLayout.NORTH);

        // Modelo para la lista
        DefaultListModel<String> modeloLista = new DefaultListModel<>();

        // Simulación de facturas
        List<Factura> facturas = new ArrayList<>();
        try {
            facturas = FacturaContr.getFacturasByClienteEmail(gmail);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (Factura factura : facturas) {
            modeloLista.addElement("Factura #" + factura.getId() +
                    " - Fecha: " + factura.getFecha() +
                    " - Cantidad: €" + factura.getCantidad() +
                    " - Estado: " + factura.getEstado());
        }

        JList<String> listaFacturas = new JList<>(modeloLista);
        JScrollPane scrollPane = new JScrollPane(listaFacturas);

        // Añadir margen inferior alrededor del scrollPane
        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // margen general
        panelLista.add(scrollPane, BorderLayout.CENTER);

        ventana.add(panelLista, BorderLayout.CENTER);

        // Botón inferior
        JButton botonCerrar = new JButton("Cerrar");
        botonCerrar.setPreferredSize(new Dimension(100, 30));
        botonCerrar.addActionListener(e -> ventana.dispose());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10)); // margen inferior
        panelBoton.add(botonCerrar);

        ventana.add(panelBoton, BorderLayout.SOUTH);

        // Centrar la ventana en pantalla
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    private List<Factura> obtenerFacturasDePrueba() {
        List<Factura> lista = new ArrayList<>();
        lista.add(new Factura(1L, "2025-05-01", 129.99f, "Pagada", 123L));
        return lista;
    }
}
