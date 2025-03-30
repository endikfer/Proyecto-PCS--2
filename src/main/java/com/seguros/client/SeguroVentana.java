package com.seguros.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.seguros.model.TipoSeguro;
import org.json.JSONObject;

public class SeguroVentana {

    private static final String hostname;
    private static final String port;
    private static final SeguroControllerClient client;

    static {
        hostname = System.getProperty("hostname", "localhost");
        port = System.getProperty("port", "8080");
        client = new SeguroControllerClient(hostname, port);
    }

    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtPrecio;
    private JComboBox<TipoSeguro> comboTipoSeguro;

    public void crearVentanaSeguro() {
        JFrame frame = new JFrame("Ventana de Seguros");
        frame.setSize(400, 350);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Inicializar los componentes
        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(10);

        JLabel lblDescripcion = new JLabel("Descripción:");
        txtDescripcion = new JTextArea(4, 20);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);

        JLabel lblTipoSeguro = new JLabel("Tipo de Seguro:");
        comboTipoSeguro = new JComboBox<>(TipoSeguro.values());
        comboTipoSeguro.setPreferredSize(new Dimension(100, 25));

        JLabel lblPrecio = new JLabel("Precio:");
        txtPrecio = new JTextField(10);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarSeguro(frame));

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

    public void cargarDatosSeguro(String seguroJson) {
        JSONObject seguro = new JSONObject(seguroJson);

        txtNombre.setText(seguro.getString("nombre"));
        txtDescripcion.setText(seguro.getString("descripcion"));
        comboTipoSeguro.setSelectedItem(TipoSeguro.valueOf(seguro.getString("tipoSeguro").toUpperCase()));
        txtPrecio.setText(String.valueOf(seguro.getDouble("precio")));
    }

    private void guardarSeguro(JFrame frame) {
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

            JOptionPane.showMessageDialog(frame, "Seguro guardado:\nNombre: " + nombre + "\nDescripción: " + descripcion
                    + "\nTipo: " + tipo + "\nPrecio: " + precio);
            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Ingrese un precio válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editarSeguro(JTextField textFieldSeguro) {
        String nombreSeguro = textFieldSeguro.getText().trim();

        if (nombreSeguro.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Obtener los datos del seguro desde el backend
            String seguroJson = client.obtenerSeguroPorNombre(nombreSeguro);

            // Cargar los datos en los campos de la ventana
            cargarDatosSeguro(seguroJson);

            // Hacer que el campo de nombre no sea editable para evitar cambiar el
            // identificador
            txtNombre.setEditable(false);

            // Mostrar la ventana para editar el seguro
            JFrame frame = new JFrame("Editar Seguro");
            frame.setSize(400, 350);
            frame.setLayout(new GridBagLayout());
            frame.setLocationRelativeTo(null);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Añadir los componentes a la ventana
            gbc.gridx = 0;
            gbc.gridy = 0;
            frame.add(new JLabel("Nombre:"), gbc);
            gbc.gridx = 1;
            frame.add(txtNombre, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            frame.add(new JLabel("Descripción:"), gbc);
            gbc.gridx = 1;
            gbc.gridwidth = 2;
            frame.add(new JScrollPane(txtDescripcion), gbc);
            gbc.gridwidth = 1;

            gbc.gridx = 0;
            gbc.gridy = 2;
            frame.add(new JLabel("Tipo de Seguro:"), gbc);
            gbc.gridx = 1;
            frame.add(comboTipoSeguro, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            frame.add(new JLabel("Precio:"), gbc);
            gbc.gridx = 1;
            frame.add(txtPrecio, gbc);

            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.addActionListener(e -> guardarSeguroEditado(frame, nombreSeguro));

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            frame.add(btnGuardar, gbc);

            frame.setVisible(true);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarSeguroEditado(JFrame frame, String nombreSeguro) {
        String descripcion = txtDescripcion.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        TipoSeguro tipo = (TipoSeguro) comboTipoSeguro.getSelectedItem();

        if (descripcion.isEmpty() || precioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Todos los campos son obligatorios.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double precio = Double.parseDouble(precioTexto);

            // Llamar al método editarSeguro del cliente
            Long id = 1L; // Aquí se debe usar el ID real del seguro obtenido del backend
            client.editarSeguro(id, nombreSeguro, descripcion, tipo.toString(), precio);

            JOptionPane.showMessageDialog(frame, "Seguro editado correctamente.");
            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Ingrese un precio válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
