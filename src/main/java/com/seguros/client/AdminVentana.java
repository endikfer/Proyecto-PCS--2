package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import java.awt.FlowLayout;
import java.awt.Color;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seguros.model.Cliente;
import com.seguros.model.Seguro;

public class AdminVentana {
    public final JFrame frame;
    public JPanel panelCentral;
    public JPanel panelSuperiorCentral;
    private final SeguroControllerAdmin seguroControllerAdmin;

    public DefaultListModel<String> modeloLista;
    public JList<String> listaSeguros;
    private DefaultListModel<String> modeloSegurosCliente;
    private JList<String> listaSegurosCliente;

    // Constructor por defecto
    public AdminVentana() {
        this(new SeguroControllerAdmin("localhost", "8080")); // Instancia por defecto
    }

    public AdminVentana(SeguroControllerAdmin seguroControllerAdmin) {
        this.seguroControllerAdmin = seguroControllerAdmin;

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

    public void cambiarContenido(int opcion) {
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

    public void mostrarContenidoSeguros() {
        // Configurar el layout del panel central
        panelCentral.setLayout(new BorderLayout());

        // Asegurarse de que el subpanel superior (botón "Cerrar sesión") esté presente
        panelCentral.add(panelSuperiorCentral, BorderLayout.NORTH);

        // Zona central: JLabel y JTextField
        JPanel panelCentro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new java.awt.Insets(10, 10, 10, 10); // Espaciado entre componentes
        gbc.anchor = GridBagConstraints.WEST;

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

        // Inicializar el modelo y la lista
        modeloLista = new DefaultListModel<>();
        listaSeguros = new JList<>(modeloLista);
        listaSeguros.setFont(new Font("Arial", Font.PLAIN, 18));
        JScrollPane scrollPane = new JScrollPane(listaSeguros); // Envolver en JScrollPane para scroll
        scrollPane.setPreferredSize(new Dimension(300, 200));
        panelCentro.add(scrollPane, gbc);

        // Llamar al método para cargar los datos en el JList
        actualizarListaSeguros();

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
        SeguroVentana ventanaCrear = new SeguroVentana(this); // Instancia de SeguroVentana
        botonCrear.addActionListener(e -> {
            ventanaCrear.crearVentanaSeguro(1); // Crear un nuevo seguro
        });

        botonEditar.addActionListener(e -> {
            String seguroSeleccionado = listaSeguros.getSelectedValue(); // Obtener el valor seleccionado
            if (seguroSeleccionado == null || seguroSeleccionado.equals("No hay seguros creados")) {
                // Mostrar mensaje si no hay selección o si la lista contiene "No hay seguros
                // creados"
                JOptionPane.showMessageDialog(frame, "No hay ningun seguro seleccionada.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                // Llamar al método para editar el seguro seleccionado
                ventanaCrear.editarSeguro(seguroSeleccionado);
            }
        });

        botonEliminar.addActionListener(e -> {
            String seguroSeleccionado = listaSeguros.getSelectedValue(); // Obtener el valor seleccionado
            if (seguroSeleccionado == null || seguroSeleccionado.equals("No hay seguros creados")) {
                JOptionPane.showMessageDialog(frame, "No hay ningun seguro seleccionada.",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                // Llamar al método para eliminar el seguro seleccionado
                ventanaCrear.eliminarSeguro(seguroSeleccionado);
                // Actualizar la lista después de eliminar
                actualizarListaSeguros();
            }
        });

        panelInferior.add(botonCrear);
        panelInferior.add(botonEditar);
        panelInferior.add(botonEliminar);

        // Agregar los subpaneles al panel central
        panelCentral.add(panelCentro, BorderLayout.CENTER);
        panelCentral.add(panelInferior, BorderLayout.SOUTH);
    }

    public void mostrarContenidoSegurosCliente() {
        panelCentral.removeAll();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.add(panelSuperiorCentral, BorderLayout.NORTH);

        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // modelo y lista para los seguros del cliente
        modeloSegurosCliente = new DefaultListModel<>();
        listaSegurosCliente = new JList<>(modeloSegurosCliente);
        listaSegurosCliente.setFont(new Font("Arial", Font.PLAIN, 16));

        // area de texto para los clientes
        JTextArea taClientes = new JTextArea();
        taClientes.setEditable(false);
        taClientes.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane spClientes = new JScrollPane(taClientes);
        spClientes.setBorder(BorderFactory.createTitledBorder("Clientes"));
        spClientes.setPreferredSize(new Dimension(250, 400));

        // scroll para la lista de seguros
        JScrollPane spSeguros = new JScrollPane(listaSegurosCliente);
        spSeguros.setBorder(BorderFactory.createTitledBorder("Seguros del cliente"));
        spSeguros.setPreferredSize(new Dimension(350, 400));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, spClientes, spSeguros);
        split.setResizeWeight(0.4);
        panelPrincipal.add(split, BorderLayout.CENTER);

        JPanel panelBoton = new JPanel();
        JButton btnEliminarSeguro = new JButton("Eliminar Seguro del Cliente");
        btnEliminarSeguro.setPreferredSize(new Dimension(250, 40));
        btnEliminarSeguro.setFont(new Font("Arial", Font.BOLD, 14));

        // ActionListener para eliminar el seguro seleccionado del cliente
        btnEliminarSeguro.addActionListener(e -> {
            String seguroSeleccionado = listaSegurosCliente.getSelectedValue();
            if (seguroSeleccionado == null || seguroSeleccionado.equals("No tiene pólizas contratadas") || seguroSeleccionado.startsWith("Error")) {
                JOptionPane.showMessageDialog(frame, "Selecciona un seguro para eliminar.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Extraer el nombre del seguro (puede ser "NombreSeguro (TIPO)")
            String nombreSeguro = seguroSeleccionado.split("\\(")[0].trim();

            // Buscar el ID del seguro a eliminar llamando a la API de seguros del cliente
            int caret = taClientes.getCaretPosition();
            int row = 0;
            try {
                row = taClientes.getLineOfOffset(caret);
            } catch (Exception ex) {
                // fallback
            }
            String[] lineas = taClientes.getText().split("\\n");
            if (row < 0 || row >= lineas.length) {
                JOptionPane.showMessageDialog(frame, "No se pudo determinar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String linea = lineas[row].trim();
            if (linea.isEmpty()) return;
            Long clienteId = Long.parseLong(linea.split("-")[0].trim());

            // Buscar el seguro por nombre para ese cliente
            new Thread(() -> {
                try {
                    java.net.URL url = new java.net.URL("http://localhost:8080/api/seguros/porCliente?clienteId=" + clienteId);
                    java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    int status = conn.getResponseCode();
                    if (status == java.net.HttpURLConnection.HTTP_OK) {
                        InputStream in = conn.getInputStream();
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        java.util.List<com.seguros.model.Seguro> seguros = mapper.readValue(
                                in, new com.fasterxml.jackson.core.type.TypeReference<java.util.List<com.seguros.model.Seguro>>() {});
                        in.close();
                        // Buscar el seguro por nombre
                        com.seguros.model.Seguro seguroAEliminar = null;
                        for (com.seguros.model.Seguro s : seguros) {
                            if (s.getNombre().equals(nombreSeguro)) {
                                seguroAEliminar = s;
                                break;
                            }
                        }
                        if (seguroAEliminar == null) {
                            javax.swing.SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                                    "No se encontró el seguro a eliminar.", "Error", JOptionPane.ERROR_MESSAGE));
                            return;
                        }
                        // Confirmar eliminación
                        int confirm = javax.swing.JOptionPane.showConfirmDialog(frame,
                                "¿Seguro que quieres eliminar el seguro '" + nombreSeguro + "' del cliente?",
                                "Confirmar eliminación", javax.swing.JOptionPane.YES_NO_OPTION);
                        if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

                        // Llamar a la API para eliminar el seguro
                        java.net.URL urlDel = new java.net.URL("http://localhost:8080/api/seguros/seguro/eliminar?id=" + seguroAEliminar.getId());
                        java.net.HttpURLConnection connDel = (java.net.HttpURLConnection) urlDel.openConnection();
                        connDel.setRequestMethod("DELETE");
                        int resp = connDel.getResponseCode();
                        connDel.disconnect();
                        if (resp == java.net.HttpURLConnection.HTTP_OK) {
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                modeloSegurosCliente.removeElement(seguroSeleccionado);
                                JOptionPane.showMessageDialog(frame, "Seguro eliminado correctamente.");
                            });
                        } else {
                            javax.swing.SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                                    "Error al eliminar el seguro. Código: " + resp, "Error", JOptionPane.ERROR_MESSAGE));
                        }
                    } else {
                        javax.swing.SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                                "Error HTTP al buscar seguros del cliente: " + status, "Error", JOptionPane.ERROR_MESSAGE));
                    }
                } catch (Exception ex) {
                    javax.swing.SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                            "Error al eliminar el seguro: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
                }
            }).start();
        });

        panelBoton.add(btnEliminarSeguro);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        panelCentral.add(panelPrincipal, BorderLayout.CENTER);

        // 1) Cargar todos los clientes desde /api/clientes
        new Thread(() -> {
            try {
                URL url = new URL("http://localhost:8080/api/clientes");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = conn.getInputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    List<Cliente> clientes = mapper.readValue(in, new TypeReference<List<Cliente>>() {
                    });
                    in.close();
                    SwingUtilities.invokeLater(() -> {
                        clientes.forEach(c -> taClientes.append(c.getId() + " - " + c.getNombre() + "\n"));
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            taClientes.setText("Error HTTP " + conn.getResponseCode());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
                conn.disconnect();
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                        "Error al cargar clientes:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();

        // 2) Listener para clicks sobre un cliente
        taClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    // calcular la línea clicada
                    int offset = taClientes.viewToModel2D(e.getPoint());
                    int row = taClientes.getLineOfOffset(offset);
                    int start = taClientes.getLineStartOffset(row);
                    int end = taClientes.getLineEndOffset(row);
                    String linea = taClientes.getText().substring(start, end).trim();
                    if (linea.isEmpty())
                        return;

                    Long clienteId = Long.parseLong(linea.split("-")[0].trim());
                    modeloSegurosCliente.clear();

                    // 3) Pedir pólizas de ese cliente
                    new Thread(() -> {
                        try {
                            URL url2 = new URL(
                                    "http://localhost:8080/api/seguros/porCliente?clienteId=" + clienteId);
                            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                            conn2.setRequestMethod("GET");
                            conn2.setRequestProperty("Accept", "application/json");
                            int status = conn2.getResponseCode();
                            if (status == HttpURLConnection.HTTP_OK) {
                                InputStream in2 = conn2.getInputStream();
                                ObjectMapper mapper2 = new ObjectMapper();
                                List<Seguro> seguros = mapper2.readValue(
                                        in2, new TypeReference<List<Seguro>>() {
                                        });
                                in2.close();
                                SwingUtilities.invokeLater(() -> {
                                    if (seguros.isEmpty()) {
                                        modeloSegurosCliente.addElement("No tiene pólizas contratadas");
                                    } else {
                                        seguros.forEach(s -> modeloSegurosCliente.addElement(
                                                s.getNombre() + " (" + s.getTipoSeguro() + ")"));
                                    }
                                });
                            } else if (status == HttpURLConnection.HTTP_NO_CONTENT) {
                                SwingUtilities.invokeLater(
                                        () -> modeloSegurosCliente.addElement("No tiene pólizas contratadas"));
                            } else {
                                SwingUtilities
                                        .invokeLater(() -> modeloSegurosCliente.addElement("Error HTTP " + status));
                            }
                            conn2.disconnect();
                        } catch (Exception ex) {
                            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                                    "Error al cargar pólizas:\n" + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE));
                        }
                    }).start();

                } catch (BadLocationException ex) {
                    // ignorar
                }
            }
        });

        panelCentral.revalidate();
        panelCentral.repaint();
    }

    public void mostrarContenidoClientesPorSeguro() {
        // Configurar layout
        panelCentral.removeAll();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.add(panelSuperiorCentral, BorderLayout.NORTH);

        // Subtítulo
        JLabel title = new JLabel("Clientes por Seguro", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panelCentral.add(title, BorderLayout.NORTH);

        // Lanzar la petición en un hilo aparte
        new Thread(() -> {
            try {
                // 1) Llamada HTTP a tu nuevo endpoint
                String url = "http://localhost:8080/api/seguros/cantidadClientes";
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // 2) Parsear el JSON recibido
                    InputStream in = conn.getInputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    List<Map<String, Object>> stats = mapper.readValue(
                            in, new TypeReference<List<Map<String, Object>>>() {
                            });
                    in.close();

                    // 3) Construir el TableModel
                    String[] columnas = { "Seguro", "Clientes contratados" };
                    DefaultTableModel model = new DefaultTableModel(columnas, 0);
                    for (Map<String, Object> row : stats) {
                        String nombreSeguro = (String) row.get("nombreSeguro");
                        Number cantidad = (Number) row.get("cantidadClientes");
                        model.addRow(new Object[] { nombreSeguro, cantidad.longValue() });
                    }

                    // 4) Crear la JTable y añadirla al panel en el hilo de Swing
                    SwingUtilities.invokeLater(() -> {
                        JTable table = new JTable(model);
                        table.setFillsViewportHeight(true);
                        JScrollPane scroll = new JScrollPane(table);
                        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        panelCentral.add(scroll, BorderLayout.CENTER);
                        panelCentral.revalidate();
                        panelCentral.repaint();
                    });
                } else {
                    // Tratamiento de error HTTP
                    SwingUtilities.invokeLater(() -> {
                        try {
                            JOptionPane.showMessageDialog(frame,
                                    "Error al obtener datos: HTTP " + conn.getResponseCode(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (HeadlessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    });
                }
                conn.disconnect();
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                        "Error de conexión: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();
    }

    public void mostrarContenidoClientes() {
        // Limpiar el panel central
        panelCentral.removeAll();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.add(panelSuperiorCentral, BorderLayout.NORTH);

        JLabel lblClientes = new JLabel("Lista de Clientes Registrados", SwingConstants.CENTER);
        lblClientes.setFont(new Font("Arial", Font.BOLD, 18));
        panelCentral.add(lblClientes, BorderLayout.NORTH);

        // Panel principal que contendrá todo
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para la lista de clientes
        DefaultListModel<String> modeloClientes = new DefaultListModel<>();
        JList<String> listaClientes = new JList<>(modeloClientes);
        listaClientes.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(listaClientes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Clientes"));
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        // Panel para el botón de eliminar
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnEliminar = new JButton("Eliminar Usuario");
        btnEliminar.setPreferredSize(new Dimension(200, 40));
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 14));

        btnEliminar.addActionListener(e -> {
            String seleccionado = listaClientes.getSelectedValue();
            if (seleccionado == null || seleccionado.startsWith("No hay")) {
                JOptionPane.showMessageDialog(frame, "Selecciona un cliente para eliminar.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(frame,
                    "¿Estás seguro de que quieres eliminar este cliente?", "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                Long idCliente = Long.parseLong(seleccionado.split("-")[0].trim());

                new Thread(() -> {
                    try {
                        URL url = new URL("http://localhost:8080/api/clientes/" + idCliente);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("DELETE");
                        int respuesta = conn.getResponseCode();

                        SwingUtilities.invokeLater(() -> {
                            if (respuesta == HttpURLConnection.HTTP_OK) {
                                JOptionPane.showMessageDialog(frame, "Cliente eliminado exitosamente.");
                                modeloClientes.removeElement(seleccionado);
                            } else if (respuesta == HttpURLConnection.HTTP_NOT_FOUND) {
                                JOptionPane.showMessageDialog(frame, "Cliente no encontrado.", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(frame, "Error al eliminar cliente. Código: " + respuesta,
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });

                        conn.disconnect();
                    } catch (Exception ex) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                                "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE));
                    }
                }).start();
            }
        });

        // 1) Cargar todos los clientes desde /api/clientes
        new Thread(() -> {
            try {
                URL url = new URL("http://localhost:8080/api/clientes");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream in = conn.getInputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    List<Cliente> clientes = mapper.readValue(in, new TypeReference<List<Cliente>>() {});
                    in.close();
                    SwingUtilities.invokeLater(() -> {
                        if (clientes.isEmpty()) {
                            modeloClientes.addElement("No hay clientes registrados.");
                        } else {
                            clientes.forEach(c -> modeloClientes.addElement(c.getId() + " - " + c.getNombre() + " - "
                                    + c.getEmail()));
                        }
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            modeloClientes.addElement("Error HTTP " + conn.getResponseCode());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                conn.disconnect();
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(frame,
                        "Error al cargar clientes:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE));
            }
        }).start();

        panelBoton.add(btnEliminar);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);

        panelCentral.add(panelPrincipal, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    public void mostrarContenidoDudas() {
        // Limpiar el panel central
        panelCentral.removeAll();
        panelCentral.setLayout(new BorderLayout());

        // Crear el modelo y la lista para la mitad izquierda
        DefaultListModel<String> modeloDudas = new DefaultListModel<>();
        System.out.println("Pedir asuntos\n");
        List<String> asuntos = seguroControllerAdmin.obtenerTodosAsuntos();
        System.out.println("Asuntos pedidos\n");
        if (asuntos != null && !asuntos.isEmpty()) {
            for (String asunto : asuntos) {
                modeloDudas.addElement(asunto);
            }
        } else {
            modeloDudas.addElement("No hay dudas registradas.");
        }
        JList<String> listaDudas = new JList<>(modeloDudas);
        listaDudas.setFont(new Font("Arial", Font.PLAIN, 16));

        // Configurar un ListCellRenderer para deshabilitar la selección del mensaje "No hay dudas registradas"
        listaDudas.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            if ("No hay dudas registradas.".equals(value)) {
                label.setEnabled(false); // Deshabilitar el elemento
                label.setForeground(Color.GRAY); // Cambiar el color para indicar que está deshabilitado
            } else {
                label.setEnabled(true);
                label.setForeground(Color.BLACK);
            }
            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
                label.setOpaque(true);
            }
            return label;
        });


        JScrollPane scrollLista = new JScrollPane(listaDudas);
        scrollLista.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Preguntas Frecuentes",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));

        // Crear el área de texto para la mitad derecha
        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false); // Solo lectura
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 16));
        areaTexto.setText("Selecciona una duda para ver más información.");
        JScrollPane scrollTexto = new JScrollPane(areaTexto);
        scrollTexto.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Detalles de la Pregunta",
                javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)));

        // Agregar un listener para actualizar el área de texto al seleccionar un
        // elemento
        listaDudas.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String seleccion = listaDudas.getSelectedValue();
                try {
                    // Llamar al método obtenerMensajeByAsunto para obtener el mensaje
                    String mensaje = seguroControllerAdmin.obtenerMensajeByAsunto(seleccion);
                    areaTexto.setText(mensaje); // Mostrar el mensaje en el JTextArea
                } catch (RuntimeException ex) {
                    // Manejar errores y mostrar un mensaje en el JTextArea
                    areaTexto.setText("Error al obtener el mensaje: " + ex.getMessage());
                }
            }
        });

        // Crear el JSplitPane para dividir el panel en dos mitades
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollLista, scrollTexto);
        splitPane.setResizeWeight(0.5); // Dividir en mitades iguales

        // Agregar el JSplitPane al panel central
        panelCentral.add(splitPane, BorderLayout.CENTER);

        // Actualizar la vista
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    public void mostrarContenidoInvalido() {
        JLabel lblInvalido = new JLabel("Opción no válida", SwingConstants.CENTER);
        lblInvalido.setFont(new Font("Arial", Font.BOLD, 16)); // Mismo estilo que el original
        panelCentral.add(lblInvalido);
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    public void actualizarListaSeguros() {
        System.out.println("Actualizando la lista de seguros...");
        modeloLista.clear(); // Limpiar el modelo antes de cargar nuevos datos
        try {
            List<Seguro> seguros = seguroControllerAdmin.listaNombreSeguros(); // Obtener lista de objetos Seguro
            if (seguros != null && !seguros.isEmpty()) {
                if (seguros.size() == 1 && "vacio".equalsIgnoreCase(seguros.get(0).getNombre())) {
                    // Si la lista contiene solo un seguro con nombre "vacio", mostrar "No hay
                    // seguros creados"
                    modeloLista.addElement("No hay seguros creados");
                    listaSeguros.setEnabled(false); // Desactivar selección
                } else {
                    // Agregar los nombres de los seguros al modelo
                    for (Seguro seguro : seguros) {
                        modeloLista.addElement(seguro.getNombre()); // Añadir solo el nombre del seguro
                    }
                    listaSeguros.setEnabled(true); // Activar selección
                }
                listaSeguros.revalidate();
                listaSeguros.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "No se encontraron seguros en la base de datos.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(frame, "Error al obtener los seguros: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}