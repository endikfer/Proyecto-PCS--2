package com.seguros.client;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;
import com.seguros.client.ui.SeguroListCellRenderer;

public class SeguroManager {

    private static final String HOSTNAME = System.getProperty("hostname", "localhost");
    private static final String PORT = System.getProperty("port", "8080");
    private static final SeguroControllerClient seguroClient = new SeguroControllerClient(HOSTNAME, PORT);

    public static void main(String[] args) {
        new InicioSesionVentana().mostrar(); // O redirige directamente al login
    }

    public static void crearVentanaPrincipal(Cliente cliente) {
        JFrame ventana = new JFrame("Gestión de Seguros");
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new BorderLayout());

        // Mostrar email del cliente logueado
        String email = inicioSesionVentana.emailInicioSesion;
        JLabel etiqueta = new JLabel("Bienvenido, " + email + " - Gestión de Seguros", SwingConstants.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 24));
        ventana.add(etiqueta, BorderLayout.NORTH);

        // Panel de pestañas para los tipos de seguros
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Crear pestañas para cada tipo de seguro
        for (TipoSeguro tipo : TipoSeguro.values()) {
            JPanel panelSeguros = crearPanelSeguros(tipo);
            tabbedPane.addTab(tipo.toString(), panelSeguros);
        }

        // Panel para mostrar todos los seguros
        JPanel panelTodos = crearPanelTodosSeguros();
        tabbedPane.addTab("Todos los Seguros", panelTodos);
    
        ventana.add(tabbedPane, BorderLayout.CENTER);

        /*JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets.set(10, 10, 10, 10);

        ventana.add(panelCentral, BorderLayout.CENTER);*/

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botonCerrarSesion = new JButton("CERRAR SESIÓN");
        botonCerrarSesion.setPreferredSize(new Dimension(200, 50));
        botonCerrarSesion.setFont(new Font("Arial", Font.BOLD, 16));

        botonCerrarSesion.addActionListener(e -> llamarLogoutClienteAPI(ventana));
        panelInferior.add(botonCerrarSesion);

        ventana.add(panelInferior, BorderLayout.SOUTH);
        ventana.setVisible(true);
    }

    private static JPanel crearPanelSeguros(TipoSeguro tipo) {
        JPanel panel = new JPanel(new BorderLayout());

        try {
            List<Seguro> seguros = seguroClient.obtenerSegurosPorTipo(tipo.toString());
            DefaultListModel<Seguro> modeloLista = new DefaultListModel<>();
            for (Seguro seguro : seguros) {
                modeloLista.addElement(seguro);
            }
            JList<Seguro> listaSeguros = new JList<>(modeloLista);
            listaSeguros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listaSeguros.setCellRenderer(new SeguroListCellRenderer());
            JScrollPane scrollPane = new JScrollPane(listaSeguros);
            panel.add(scrollPane, BorderLayout.CENTER);

            JButton btnSeleccionar = new JButton("Seleccionar Seguro");
            btnSeleccionar.addActionListener(e -> {
                Seguro seguroSeleccionado = listaSeguros.getSelectedValue();
                if (seguroSeleccionado != null) {
                    abrirVentanaSeguro(seguroSeleccionado);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor seleccione un seguro", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panel.add(btnSeleccionar, BorderLayout.SOUTH);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los seguros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    private static JPanel crearPanelTodosSeguros() {
        JPanel panel = new JPanel(new BorderLayout());

        try {
            List<Seguro> seguros = seguroClient.obtenerTodosSeguros();
            DefaultListModel<Seguro> modeloLista = new DefaultListModel<>();
            for (Seguro seguro : seguros) {
                modeloLista.addElement(seguro);
            }
            JList<Seguro> listaSeguros = new JList<>(modeloLista);
            listaSeguros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listaSeguros.setCellRenderer(new SeguroListCellRenderer());
            JScrollPane scrollPane = new JScrollPane(listaSeguros);
            panel.add(scrollPane, BorderLayout.CENTER);

            JButton btnSeleccionar = new JButton("Seleccionar Seguro");
            btnSeleccionar.addActionListener(e -> {
                Seguro seguroSeleccionado = listaSeguros.getSelectedValue();
                if (seguroSeleccionado != null) {
                    abrirVentanaSeguro(seguroSeleccionado);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor seleccione un seguro", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panel.add(btnSeleccionar, BorderLayout.SOUTH);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los seguros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    private static void abrirVentanaSeguro(Seguro seguro) {
        switch (seguro.getTipoSeguro()) {
            case COCHE:
                new SeguroCocheVentana(seguro).mostrar();
                break;
            case VIDA:
                new SeguroVidaVentana(seguro).mostrar();
                break;
            case CASA:
                new SeguroCasaVentana(seguro).mostrar();
                break;
            default:
                JOptionPane.showMessageDialog(null, "Tipo de seguro no reconocido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void llamarLogoutClienteAPI(JFrame ventana) {
        try {
            String urlCompleta = "http://" + HOSTNAME + ":" + PORT + "/api/clientes/logout";
            java.net.URL url = new java.net.URL(urlCompleta);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(null, "Sesión cerrada correctamente.");
                ventana.dispose();
                new InicioSesionVentana().mostrar();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Error al cerrar sesión. Código: " + responseCode,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la API: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}