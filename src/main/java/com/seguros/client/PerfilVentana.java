package com.seguros.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.List;

import com.seguros.model.Cliente;
import com.seguros.model.Seguro;



import com.seguros.client.SeguroControllerClient;

public class PerfilVentana extends JFrame {

    private final Cliente cliente;
    private JTextArea segurosArea;

    public PerfilVentana(Cliente cliente) {
        setTitle("Perfil del Cliente");
        setSize(450, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Datos del Perfil", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombreLabel = new JLabel("Nombre: " + cliente.getNombre());
        nombreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email: " + cliente.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Seguros contratados
        JLabel segurosLabel = new JLabel("Seguros Contratados:");
        segurosLabel.setFont(new Font("Arial", Font.BOLD, 18));
        segurosLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea segurosArea = new JTextArea(10, 30);
        segurosArea.setEditable(false);
        segurosArea.setLineWrap(true);
        segurosArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(segurosArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        
     // Crear el controlador una única vez
        FacturaControllerClient factCtrl = new FacturaControllerClient("localhost", "8080");
        
        
        // Botón imprimir factura
        JButton imprimirBtn = new JButton("Imprimir Factura");
        imprimirBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        imprimirBtn.setMnemonic('I');
        imprimirBtn.setToolTipText("Descarga en txt la factura de tus seguros contratados");
        imprimirBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Guardar factura");
            fc.setSelectedFile(new File("factura_" + cliente.getId() + ".txt"));
            if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    factCtrl.descargarFactura(cliente.getId(),
                                               (Path) fc.getSelectedFile().toPath());
                    JOptionPane.showMessageDialog(null, "Factura descargada correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null,
                            "Error al descargar factura:\n" + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Cargar seguros del cliente
        try {
            SeguroControllerClient seguroClient = new SeguroControllerClient("localhost", "8080"); // Cambia host/puerto
                                                                                                   // si es necesario
            List<Seguro> segurosCliente = seguroClient.obtenerSegurosPorCliente(cliente.getId());

            if (segurosCliente.isEmpty()) {
                segurosArea.setText("No tienes seguros contratados.");
            } else {
                for (Seguro seguro : segurosCliente) {
                    segurosArea.append("- " + seguro.getNombre() + " (" + seguro.getTipoSeguro() + ")\n");
                }
            }
        } catch (Exception e) {
            segurosArea.setText("Error al cargar los seguros.");
            System.err.println("Error cargando seguros: " + e.getMessage());
        }

        panel.add(Box.createVerticalStrut(20));
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(nombreLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(20));
        panel.add(segurosLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(scrollPane);
        panel.add(Box.createVerticalStrut(20));
        panel.add(imprimirBtn);

        add(panel);
        this.cliente = null;
    }

     

    public void mostrar() {
        setVisible(true);
    }
}