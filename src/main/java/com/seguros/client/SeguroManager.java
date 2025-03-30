package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.seguros.model.TipoSeguro;

public class SeguroManager {

    public static void main(String[] args) {
        // RegistroVentana registroVentana = new RegistroVentana();
        // registroVentana.mostrar();

        crearVentanaPrincipal();
    }

    @SuppressWarnings("Convert2Lambda")
    public static void crearVentanaPrincipal() {
        // Crear el marco (ventana)
        JFrame ventana = new JFrame("Gestión de Seguros");

        // Configurar la ventana para que ocupe toda la pantalla
        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza la ventana
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana

        // Opcional: Establecer un diseño o contenido
        ventana.setLayout(new BorderLayout());
        JLabel etiqueta = new JLabel("Bienvenido a la Gestión de Seguros", SwingConstants.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 24));
        ventana.add(etiqueta, BorderLayout.NORTH);

        // Crear un panel central para el texto y el campo de entrada
        JPanel panelCentral = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets.set(10, 10, 10, 10);

        JLabel labelSeguro = new JLabel("Seguro a modificar:");
        labelSeguro.setFont(new Font("Arial", Font.PLAIN, 18));
        panelCentral.add(labelSeguro, gbc);

        gbc.gridx = 1;
        JTextField textFieldSeguro = new JTextField();
        textFieldSeguro.setPreferredSize(new Dimension(300, 30));
        panelCentral.add(textFieldSeguro, gbc);

        // Añadir el panel central a la ventana
        ventana.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centra los botones
        JButton botonCrear = new JButton("CREAR");
        botonCrear.setPreferredSize(new Dimension(150, 50)); // Tamaño del botón (ancho, alto)
        botonCrear.setFont(new Font("Arial", Font.BOLD, 16));

        JButton botonEditar = new JButton("EDITAR");
        botonEditar.setPreferredSize(new Dimension(150, 50)); // Tamaño del botón (ancho, alto)
        botonEditar.setFont(new Font("Arial", Font.BOLD, 16));

        // Añadir ActionListener al botón CREAR
        botonCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón CREAR presionado");
                // Aquí puedes agregar la lógica para crear un seguro
                crearVentanaSeguro();
            }
        });

        // Añadir ActionListener al botón EDITAR
        botonEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón EDITAR presionado");
                // Aquí puedes agregar la lógica para editar un seguro

            }
        });

        panelInferior.add(botonCrear);
        panelInferior.add(botonEditar);

        // Añadir el panel inferior a la ventana
        ventana.add(panelInferior, BorderLayout.SOUTH);

        // Hacer visible la ventana
        ventana.setVisible(true);
    }

    public static void crearVentanaSeguro() {
        String hostname = System.getProperty("hostname", "localhost");
        String port = System.getProperty("port", "8080");

        SeguroControllerClient client = new SeguroControllerClient(hostname, port);
        JFrame frame = new JFrame("Ventana de Seguros");
        frame.setSize(400, 350);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null); // Centrar la ventana

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes de la ventana
        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(10);

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextArea txtDescripcion = new JTextArea(4, 20);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

        JLabel lblTipoSeguro = new JLabel("Tipo de Seguro:");
        JComboBox<TipoSeguro> comboTipoSeguro = new JComboBox<>(TipoSeguro.values());
        comboTipoSeguro.setPreferredSize(new Dimension(100, 25));

        JLabel lblPrecio = new JLabel("Precio:");
        JTextField txtPrecio = new JTextField(10);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            String precioTexto = txtPrecio.getText().trim();
            TipoSeguro tipo = (TipoSeguro) comboTipoSeguro.getSelectedItem();

            if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Todos los campos son obligatorios.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double precio = Double.parseDouble(precioTexto);

                client.crearSeguro(nombre, descripcion, tipo.toString(), precio);

                JOptionPane.showMessageDialog(frame, "Seguro guardado:\nNombre: " + nombre + "\nDescripción: "
                        + descripcion + "\nTipo: " + tipo + "\nPrecio: " + precio);

                frame.dispose(); // Cerrar la ventana después de guardar
                System.out.println("El tipo de seguro es " + tipo.toString());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ingrese un precio válido", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                // Mostrar el mensaje de error devuelto por el servidor
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(frame, "Error inesperado: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(lblNombre, gbc);
        gbc.gridx = 1;
        frame.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(lblDescripcion, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(scrollDescripcion, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(lblTipoSeguro, gbc);
        gbc.gridx = 1;
        frame.add(comboTipoSeguro, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(lblPrecio, gbc);
        gbc.gridx = 1;
        frame.add(txtPrecio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(btnGuardar, gbc);

        // Mostrar ventana
        frame.setVisible(true);
    }

    public static void mostrarVentanaSeguroConDatos(String nombre, String descripcion, TipoSeguro tipo, double precio) {
        JFrame frame = new JFrame("Editar Seguro");
        frame.setSize(400, 350);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNombre = new JLabel("Nombre:");
        JTextField txtNombre = new JTextField(10);
        txtNombre.setText(nombre);

        JLabel lblDescripcion = new JLabel("Descripción:");
        JTextArea txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setText(descripcion);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

        JLabel lblTipoSeguro = new JLabel("Tipo de Seguro:");
        JComboBox<TipoSeguro> comboTipoSeguro = new JComboBox<>(TipoSeguro.values());
        comboTipoSeguro.setSelectedItem(tipo);
        comboTipoSeguro.setPreferredSize(new Dimension(100, 25));

        JLabel lblPrecio = new JLabel("Precio:");
        JTextField txtPrecio = new JTextField(10);
        txtPrecio.setText(String.valueOf(precio));

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> {
            String nuevoNombre = txtNombre.getText();
            String nuevaDescripcion = txtDescripcion.getText();
            TipoSeguro nuevoTipo = (TipoSeguro) comboTipoSeguro.getSelectedItem();

            try {
                double nuevoPrecio = Double.parseDouble(txtPrecio.getText());
                JOptionPane.showMessageDialog(frame, "Seguro actualizado:\nNombre: " + nuevoNombre + "\nDescripción: "
                        + nuevaDescripcion + "\nTipo: " + nuevoTipo + "\nPrecio: " + nuevoPrecio);
                frame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Ingrese un precio válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(lblNombre, gbc);
        gbc.gridx = 1;
        frame.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(lblDescripcion, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        frame.add(scrollDescripcion, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(lblTipoSeguro, gbc);
        gbc.gridx = 1;
        frame.add(comboTipoSeguro, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(lblPrecio, gbc);
        gbc.gridx = 1;
        frame.add(txtPrecio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(btnGuardar, gbc);

        frame.setVisible(true);
    }

}
