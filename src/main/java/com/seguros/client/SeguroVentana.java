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

import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;
import org.json.JSONObject;

public class SeguroVentana {

    private static final String hostname;
    private static final String port;
    private static final SeguroControllerAdmin admin;
    private AdminVentana adminVentana;

    public SeguroVentana(AdminVentana adminVentana) {
        this.adminVentana = adminVentana; // Guardar la instancia de AdminVentana
    }
    public SeguroVentana(){// Inicializar adminVentana como null
    }

    static {
        hostname = System.getProperty("hostname", "localhost");
        port = System.getProperty("port", "8080");
        admin = new SeguroControllerAdmin(hostname, port);
    }

    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtPrecio;
    private JComboBox<TipoSeguro> comboTipoSeguro;
    private Long idSeguro; // ID del seguro a editar (si es necesario)

    JButton btnGuardar;

    public void crearVentanaSeguro(int Editar) {
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

        if(Editar == 2){
            btnGuardar = new JButton("Salir");
        }else{
            btnGuardar = new JButton("Guardar");
        }
        btnGuardar.addActionListener(e -> {
            if (Editar == 0) {
                guardarSeguroEditado(frame, txtNombre.getText().trim());
            } else if (Editar == 1) {
                guardarSeguro(frame);
            } else{
                frame.dispose();
            }
            adminVentana.actualizarListaSeguros();
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

        String nombre_final = sustituirEspacios(nombre);
        String descripcion_final = sustituirEspacios(descripcion);
        
        String precioTexto = txtPrecio.getText().trim();
        TipoSeguro tipo = (TipoSeguro) comboTipoSeguro.getSelectedItem();

        if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Todos los campos son obligatorios.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double precio = Double.parseDouble(precioTexto);
            admin.crearSeguro(nombre_final, descripcion_final, tipo.toString(), precio);

            JOptionPane.showMessageDialog(frame, "Seguro guardado:\nNombre: " + nombre + "\nDescripción: " + descripcion
                    + "\nTipo: " + tipo + "\nPrecio: " + precio);
            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Ingrese un precio válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editarSeguro(String nombreSeguro) {
        if (nombreSeguro == null || nombreSeguro.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            
            // Obtener los datos del seguro desde el backend
            System.out.println("Obteniendo seguro por nombre: " + nombreSeguro);
            Seguro seguro = admin.obtenerSeguroPorNombre(sustituirEspacios(nombreSeguro)); // Trim para eliminar espacios

            crearVentanaSeguro(0);
            System.out.println("Seguro obtenido: " + seguro);

            idSeguro = seguro.getId(); // Guardar el ID del seguro para editarlo más tarde

            txtNombre.setText(nombreSeguro);
            txtDescripcion.setText(seguro.getDescripcion());
            comboTipoSeguro.setSelectedItem(seguro.getTipoSeguro());
            txtPrecio.setText(String.valueOf(seguro.getPrecio()));
           

            // Hacer que el campo de nombre no sea editable para evitar cambiar el
            // identificador
            //txtNombre.setEditable(false);
            

            // Mostrar la ventana para editar el seguro
        }catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el seguro: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        
    }

    private void guardarSeguroEditado(JFrame frame, String nombreSeguro) {
        String descripcion = txtDescripcion.getText().trim();
        String precioTexto = txtPrecio.getText().trim();
        String tipo = comboTipoSeguro.getSelectedItem().toString();

        if (descripcion.isEmpty() || precioTexto.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Todos los campos son obligatorios.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double precio = Double.parseDouble(precioTexto);

            // Llamar al método editarSeguro del cliente
            
            admin.editarSeguro(idSeguro, nombreSeguro, descripcion.trim(), tipo.toString(), precio);

            JOptionPane.showMessageDialog(frame, "Seguro editado correctamente.");
            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Ingrese un precio válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static String sustituirEspacios(String texto) {
        if (texto == null) return null;
        System.out.println("Texto original: " + texto); // Imprimir el texto original
        System.out.println("Texto sustituido: " + texto.replace(' ', ';')); // Imprimir el texto sustituido
        return texto.replace(' ', ';');
    }

    public void eliminarSeguro(String nombreSeguro) {
        if (nombreSeguro == null || nombreSeguro.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            // Obtener el seguro desde el backend
            System.out.println("Buscando seguro por nombre: " + nombreSeguro);
            Seguro seguro = admin.obtenerSeguroPorNombre(sustituirEspacios(nombreSeguro));
    
            if (seguro == null) {
                JOptionPane.showMessageDialog(null, "No se encontró ningún seguro con ese nombre.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            // Confirmar eliminación
            int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar el seguro \"" + nombreSeguro + "\"?",
                                                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    
            if (confirm == JOptionPane.YES_OPTION) {
                admin.eliminarSeguro(seguro.getId()); // Método que deberías tener en tu backend
                JOptionPane.showMessageDialog(null, "Seguro eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
    
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el seguro: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    public void informacionSeguro(String nombreSeguro) {
        if (nombreSeguro == null || nombreSeguro.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            
            // Obtener los datos del seguro desde el backend
            System.out.println("Obteniendo seguro por nombre: " + nombreSeguro);
            Seguro seguro = admin.obtenerSeguroPorNombre(sustituirEspacios(nombreSeguro)); // Trim para eliminar espacios

            crearVentanaSeguro(2);
            System.out.println("Seguro obtenido: " + seguro);

            txtNombre.setText(nombreSeguro);
            txtDescripcion.setText(seguro.getDescripcion());
            comboTipoSeguro.setSelectedItem(seguro.getTipoSeguro());
            txtPrecio.setText(String.valueOf(seguro.getPrecio()));
            
            txtNombre.setEditable(false); // Hacer que el campo de nombre no sea editable para evitar cambiar el identificador
            txtDescripcion.setEditable(false); // Hacer que el campo de descripción no sea editable
            txtPrecio.setEditable(false); // Hacer que el campo de precio no sea editable
            comboTipoSeguro.setEnabled(false); // Hacer que el combo de tipo de seguro no sea editable

            // Mostrar la ventana para editar el seguro
        }catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el seguro: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        
    }

}
