package com.seguros.client;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import com.seguros.model.*;
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

        JLabel etiqueta = new JLabel("Bienvenido, " + cliente.getEmail() + " - Gestión de Seguros", SwingConstants.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 24));
        ventana.add(etiqueta, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        for (TipoSeguro tipo : TipoSeguro.values()) {
            JPanel panelSeguros = crearPanelSeguros(tipo);
            tabbedPane.addTab(tipo.toString(), panelSeguros);
        }

        JPanel panelTodos = crearPanelTodosSeguros();
        tabbedPane.addTab("Todos los Seguros", panelTodos);

        ventana.add(tabbedPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton botonPerfil = new JButton("PERFIL");
        botonPerfil.setPreferredSize(new Dimension(200, 50));
        botonPerfil.setFont(new Font("Arial", Font.BOLD, 16));
        botonPerfil.addActionListener(e -> new PerfilVentana(cliente).mostrar());
        panelInferior.add(botonPerfil);

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