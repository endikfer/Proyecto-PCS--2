package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class AdminVentana {
    private final JFrame frame;
    private final JPanel panelCentral;
    private final JPanel panelSuperiorCentral;

    public AdminVentana() {
        frame = new JFrame("Panel de Administrador");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Panel izquierdo con botones
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Los botones ocuparán todo el espacio disponible
        gbc.weightx = 1.0; // Expandir horizontalmente
        gbc.weighty = 1.0; // Expandir verticalmente

        // Crear botones
        String[] nombresBotones = { "Seguros", "Seguros cliente", "Clientes por seguro", "Clientes", "Dudas" };
        for (int i = 0; i < nombresBotones.length; i++) {
            JButton boton = new JButton(nombresBotones[i]);
            gbc.gridy = i; // Posición vertical
            gbc.gridx = 0; // Asegurar que los botones estén en la primera columna
            panelIzquierdo.add(boton, gbc);

            // Agregar acción al botón
            int opcion = i + 1;
            boton.addActionListener((ActionEvent e) -> cambiarContenido(opcion));
        }

        // Panel central inicial
        panelCentral = new JPanel(new BorderLayout());

        // Subpanel superior dentro del panel central
        panelSuperiorCentral = new JPanel(new BorderLayout());
        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.addActionListener((ActionEvent e) -> llamarLogoutAPI());
        panelSuperiorCentral.add(btnCerrarSesion, BorderLayout.EAST); // Botón alineado a la derecha
        panelCentral.add(panelSuperiorCentral, BorderLayout.NORTH); // Subpanel superior fijo

        // Mostrar contenido inicial
        mostrarContenidoSeguros();

        // Agregar paneles al frame
        frame.add(panelIzquierdo, BorderLayout.WEST); // Panel izquierdo fijo
        frame.add(panelCentral, BorderLayout.CENTER); // Panel central dinámico
    }

    private void llamarLogoutAPI() {
        SwingUtilities.invokeLater(() -> {
            try {
                @SuppressWarnings("deprecation")
                URL url = new URL("http://localhost:8080/api/admin/logout");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true); // Muy importante para una petición POST

                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    JOptionPane.showMessageDialog(frame, "Sesión cerrada correctamente.");
                    frame.dispose();
                    new InicioSesionVentana().mostrar();
                } else {
                    JOptionPane.showMessageDialog(frame, "Error al cerrar sesión. Código: " + responseCode,
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

                connection.disconnect();

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error de conexión: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cambiarContenido(int opcion) {
        // Limpiar el panel central
        panelCentral.removeAll();
        panelCentral.add(panelSuperiorCentral, BorderLayout.NORTH);

        // Llamar al método correspondiente según la opción seleccionada
        switch (opcion) {
            case 1 -> mostrarContenidoSeguros();
            case 2 -> mostrarContenidoSegurosCliente();
            case 3 -> mostrarContenidoClientesPorSeguro();
            case 4 -> mostrarContenidoClientes();
            case 5 -> mostrarContenidoDudas();
            default -> mostrarContenidoInvalido();
        }

        // Actualizar la vista
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private void mostrarContenidoSeguros() {
        // Configurar el layout del panel central
        panelCentral.setLayout(new BorderLayout());

        // Asegurarse de que el subpanel superior (botón "Cerrar sesión") esté presente
        panelCentral.add(panelSuperiorCentral, BorderLayout.NORTH);

        // Zona central: JLabel y JTextField
        JPanel panelCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10); // Espaciado entre componentes
        gbc.anchor = GridBagConstraints.WEST;

        // Agregar JLabel a la izquierda
        /*
         * gbc.gridx = 0;
         * gbc.gridy = 0;
         * JLabel labelSeguro = new JLabel("Seguro a modificar:");
         * labelSeguro.setFont(new Font("Arial", Font.PLAIN, 18));
         * panelCentro.add(labelSeguro, gbc);
         * 
         * // Agregar JTextField a la derecha del JLabel
         * gbc.gridx = 1;
         * JTextField textFieldSeguro = new JTextField();
         * textFieldSeguro.setPreferredSize(new java.awt.Dimension(300, 30));
         * panelCentro.add(textFieldSeguro, gbc);
         */

        // Agregar JList con 5 ejemplos
        gbc.gridx = 0;
        gbc.gridy = 0; // Posición en la primera fila
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.weightx = 0; // No expandir horizontalmente
        gbc.weighty = 0; // No expandir verticalmente
        gbc.fill = GridBagConstraints.NONE; // No expandir el componente
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel cabecera = new JLabel("Seguros disponibles");
        cabecera.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente grande y negrita
        panelCentro.add(cabecera, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1; // Mover la lista a la fila siguiente
        gbc.gridwidth = 2; // La lista ocupará dos columnas
        gbc.weightx = 1.0; // Expandir horizontalmente
        gbc.weighty = 1.0; // Expandir verticalmente
        gbc.fill = GridBagConstraints.BOTH;

        /*DefaultListModel<String> modeloLista = new DefaultListModel<>();
        modeloLista.addElement("Seguro 1");
        modeloLista.addElement("Seguro 2");
        modeloLista.addElement("Seguro 3");
        modeloLista.addElement("Seguro 4");
        modeloLista.addElement("Seguro 5");
        modeloLista.addElement("Seguro 6");*/

        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        try {
            SeguroControllerAdmin seguroControllerAdmin = new SeguroControllerAdmin("localhost", "8080");
            List<String> nombresSeguros = seguroControllerAdmin.listaNombreSeguros();
            if (nombresSeguros != null && !nombresSeguros.isEmpty()) {
                if (nombresSeguros.size() == 1 && "vacio".equalsIgnoreCase(nombresSeguros.get(0))) {
                    // Si el servidor devuelve "vacio", mostrar "No hay seguros creados"
                    modeloLista.addElement("No hay seguros creados");
                } else {
                    // Agregar los nombres de los seguros al modelo
                    for (String nombre : nombresSeguros) {
                        modeloLista.addElement(nombre);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No se encontraron seguros en la base de datos.", 
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(frame, "Error al obtener los seguros: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        JList<String> listaSeguros = new JList<>(modeloLista);
        if (modeloLista.size() == 1 && modeloLista.getElementAt(0).equals("No hay seguros creados")) {
            listaSeguros.setEnabled(false); // Desactivar selección si no hay seguros
        } else {
            listaSeguros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Selección única
        }
        listaSeguros.setFont(new Font("Arial", Font.PLAIN, 18));

        JScrollPane scrollPane = new JScrollPane(listaSeguros); // Envolver en JScrollPane para scroll
        scrollPane.setPreferredSize(new Dimension(300, 200));
        panelCentro.add(scrollPane, gbc);

        // Zona inferior: Botones "CREAR" y "EDITAR"
        JPanel panelInferior = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER));
        JButton botonCrear = new JButton("CREAR");
        botonCrear.setPreferredSize(new java.awt.Dimension(150, 50));
        botonCrear.setFont(new Font("Arial", Font.BOLD, 16));

        JButton botonEditar = new JButton("EDITAR");
        botonEditar.setPreferredSize(new java.awt.Dimension(150, 50));
        botonEditar.setFont(new Font("Arial", Font.BOLD, 16));

        JButton botonEliminar = new JButton("ELIMINAR");
        botonEliminar.setPreferredSize(new java.awt.Dimension(150, 50));
        botonEliminar.setFont(new Font("Arial", Font.BOLD, 16));

        // Listeners para los botones
        SeguroVentana ventanaCrear = new SeguroVentana(); // Instancia de SeguroVentana
        botonCrear.addActionListener(e -> ventanaCrear.crearVentanaSeguro(false));
        // botonEditar.addActionListener(e ->
        // ventanaCrear.editarSeguro(textFieldSeguro.getText()));

        panelInferior.add(botonCrear);
        panelInferior.add(botonEditar);
        panelInferior.add(botonEliminar);

        // Agregar los subpaneles al panel central
        panelCentral.add(panelCentro, BorderLayout.CENTER);
        panelCentral.add(panelInferior, BorderLayout.SOUTH);
    }

    private void mostrarContenidoSegurosCliente() {
        JLabel lblSegurosCliente = new JLabel("Contenido de la opción 2: Seguros cliente", SwingConstants.CENTER);
        lblSegurosCliente.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblSegurosCliente);
    }

    private void mostrarContenidoClientesPorSeguro() {
        JLabel lblClientesPorSeguro = new JLabel("Contenido de la opción 3: Clientes por seguro",
                SwingConstants.CENTER);
        lblClientesPorSeguro.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblClientesPorSeguro);
    }

    private void mostrarContenidoClientes() {
        JLabel lblClientes = new JLabel("Contenido de la opción 4: Clientes", SwingConstants.CENTER);
        lblClientes.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblClientes);
    }

    private void mostrarContenidoDudas() {
        JLabel lblDudas = new JLabel("Contenido de la opción 5: Dudas", SwingConstants.CENTER);
        lblDudas.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblDudas);
    }

    private void mostrarContenidoInvalido() {
        JLabel lblInvalido = new JLabel("Opción no válida", SwingConstants.CENTER);
        lblInvalido.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblInvalido);
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}