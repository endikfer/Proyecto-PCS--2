package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
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
import javax.swing.SwingUtilities;

import com.seguros.model.Cliente;
import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;
import com.seguros.client.ui.SeguroListCellRenderer;

public class SeguroManager {

    private final String HOSTNAME = System.getProperty("hostname", "localhost");
    private final String PORT = System.getProperty("port", "8080");
    public final InicioSesionVentana inicioSesionVentana = new InicioSesionVentana();
    public SeguroControllerClient seguroClient = new SeguroControllerClient(HOSTNAME, PORT);

    public static void main(String[] args) {
        // Siempre que trabajes con Swing, arráncalo en el Event Dispatch Thread:
        SwingUtilities.invokeLater(() -> {
            new SeguroManager().inicioSesionVentana.mostrar();
        });
    }

    public void crearVentanaPrincipal(Cliente cliente) {
        JFrame ventana = new JFrame("Gestión de Seguros");
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new BorderLayout());

        // Panel superior con etiqueta de bienvenida y botón de cerrar sesión
        JPanel panelSuperior = new JPanel(new BorderLayout());

        // Botón de perfil
        JButton btnPerfil = new JButton("Ver Perfil");
        btnPerfil.addActionListener(e -> {
            Cliente clienteActualizado = seguroClient.obtenerDatosCliente(cliente.getEmail());
            new PerfilVentana(clienteActualizado).mostrar();
        });
        panelSuperior.add(btnPerfil, BorderLayout.WEST);

        // Mostrar email del cliente logueado
        String email = inicioSesionVentana.emailInicioSesion;
        JLabel etiqueta = new JLabel("Bienvenido, " + email + " - Gestión de Seguros", SwingConstants.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 24));
        panelSuperior.add(etiqueta, BorderLayout.CENTER);

        // Botón de cerrar sesión
        JButton botonCerrarSesion = new JButton("Cerrar Sesión");
        botonCerrarSesion.setPreferredSize(new Dimension(150, 40));
        botonCerrarSesion.setFont(new Font("Arial", Font.BOLD, 14));
        botonCerrarSesion.setFocusPainted(false);
        botonCerrarSesion.addActionListener(e -> llamarLogoutClienteAPI(ventana));

        // Panel para posicionar el botón con separación
        JPanel panelBoton = new JPanel(new BorderLayout());
        panelBoton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10));
        panelBoton.add(botonCerrarSesion, BorderLayout.EAST);

        panelSuperior.add(panelBoton, BorderLayout.EAST);

        ventana.add(panelSuperior, BorderLayout.NORTH);

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

        // Envolver el JTabbedPane en un JPanel con márgenes
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Márgenes izquierdo y derecho
        panelCentral.add(tabbedPane, BorderLayout.CENTER);

        ventana.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con el botón "Seleccionar Seguro"
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSeleccionar = new JButton("Seleccionar Seguro");
        btnSeleccionar.setPreferredSize(new Dimension(200, 40));
        btnSeleccionar.setFont(new Font("Arial", Font.BOLD, 14));

        JButton botonInfomracion = new JButton("Información del Seguro");
        botonInfomracion.setPreferredSize(new Dimension(200, 40));
        botonInfomracion.setFont(new Font("Arial", Font.BOLD, 14));

        SeguroVentana ventanaCrear = new SeguroVentana();

        btnSeleccionar.addActionListener(e -> {
            // Obtener la pestaña seleccionada
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1) {
                JPanel selectedPanel = (JPanel) tabbedPane.getComponentAt(selectedIndex);
                @SuppressWarnings("unchecked")
                JList<Seguro> listaSeguros = (JList<Seguro>) ((JScrollPane) selectedPanel.getComponent(0)).getViewport()
                        .getView();
                Seguro seguroSeleccionado = listaSeguros.getSelectedValue();
                if (seguroSeleccionado != null) {
                    abrirVentanaSeguro(seguroSeleccionado);
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor seleccione un seguro", "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        botonInfomracion.addActionListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1) {
                JPanel selectedPanel = (JPanel) tabbedPane.getComponentAt(selectedIndex);
                @SuppressWarnings("unchecked")
                JList<Seguro> listaSeguros = (JList<Seguro>) ((JScrollPane) selectedPanel.getComponent(0)).getViewport()
                        .getView();
                Seguro seguroSeleccionado = listaSeguros.getSelectedValue();
                if (seguroSeleccionado != null) {

                    ventanaCrear.informacionSeguro(seguroSeleccionado.getNombre());

                } else {
                    JOptionPane.showMessageDialog(null, "Por favor seleccione un seguro", "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                }
            }

        });

        panelInferior.add(btnSeleccionar);
        panelInferior.add(botonInfomracion);

        ventana.add(panelInferior, BorderLayout.SOUTH);

        ventana.setVisible(true);
    }

    public JPanel crearPanelSeguros(TipoSeguro tipo) {
        JPanel panel = new JPanel(new BorderLayout());

        try {
            // Obtener seguros por tipo desde el backend
            List<Seguro> seguros = seguroClient.obtenerSegurosPorTipo(tipo.toString());

            DefaultListModel<Seguro> modeloLista = new DefaultListModel<>();
            if (seguros.size() == 1 && "vacio".equalsIgnoreCase(seguros.get(0).getNombre())) {
                // Si la lista contiene solo un seguro con nombre "vacio", mostrar mensaje
                JLabel mensaje = new JLabel("No hay seguros de este tipo disponibles.", SwingConstants.CENTER);
                mensaje.setFont(new Font("Arial", Font.BOLD, 16));
                panel.add(mensaje, BorderLayout.CENTER);
            } else {
                // Añadir los seguros al modelo de la lista
                for (Seguro seguro : seguros) {
                    modeloLista.addElement(seguro);
                }

                JList<Seguro> listaSeguros = new JList<>(modeloLista);
                listaSeguros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listaSeguros.setCellRenderer(new SeguroListCellRenderer());

                JScrollPane scrollPane = new JScrollPane(listaSeguros);
                panel.add(scrollPane, BorderLayout.CENTER);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los seguros: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    public JPanel crearPanelTodosSeguros() {
        JPanel panel = new JPanel(new BorderLayout());

        try {
            // Obtener todos los seguros desde el backend
            List<Seguro> seguros = seguroClient.obtenerTodosSeguros();

            DefaultListModel<Seguro> modeloLista = new DefaultListModel<>();

            if (seguros.size() == 1 && "vacio".equalsIgnoreCase(seguros.get(0).getNombre())) {
                // Si la lista contiene solo un seguro con nombre "vacio", mostrar mensaje
                JLabel mensaje = new JLabel("No hay seguros de ningun tipo disponibles.", SwingConstants.CENTER);
                mensaje.setFont(new Font("Arial", Font.BOLD, 16));
                panel.add(mensaje, BorderLayout.CENTER);
            } else {
                for (Seguro seguro : seguros) {
                    modeloLista.addElement(seguro);
                }

                JList<Seguro> listaSeguros = new JList<>(modeloLista);
                listaSeguros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                listaSeguros.setCellRenderer(new SeguroListCellRenderer());

                JScrollPane scrollPane = new JScrollPane(listaSeguros);
                panel.add(scrollPane, BorderLayout.CENTER);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los seguros: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    public static void abrirVentanaSeguro(Seguro seguro) {
        if (seguro.getTipoSeguro() == null) {
            JOptionPane.showMessageDialog(null, "Tipo de seguro no reconocido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (seguro.getTipoSeguro()) {
            case COCHE -> new SeguroCocheVentana(seguro).mostrar();
            case VIDA -> new SeguroVidaVentana(seguro).mostrar();
            case CASA -> new SeguroCasaVentana(seguro).mostrar();
            default ->
                JOptionPane.showMessageDialog(null, "Tipo de seguro no reconocido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void llamarLogoutClienteAPI(JFrame ventana) {
        try {
            String urlCompleta = "http://" + HOSTNAME + ":" + PORT + "/api/clientes/logout";
            @SuppressWarnings("deprecation")
            java.net.URL url = new java.net.URL(urlCompleta);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            if (responseCode == java.net.HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(null, "Sesión cerrada correctamente.");
                ventana.dispose();
                inicioSesionVentana.mostrar();
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
