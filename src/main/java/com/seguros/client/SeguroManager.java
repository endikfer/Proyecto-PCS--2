package com.seguros.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SeguroManager {

    private static final String hostname;
    private static final String port;
    private static final SeguroControllerClient client;
    private static final InicioSesionVentana inicioSesionVentana;

    static {
        hostname = System.getProperty("hostname", "localhost");
        port = System.getProperty("port", "8080");
        client = new SeguroControllerClient(hostname, port);
        inicioSesionVentana = new InicioSesionVentana();
    }

    public static void main(String[] args) {
        //RegistroVentana registroVentana = new RegistroVentana();
        //registroVentana.mostrar();

        //inicioSesionVentana.mostrar();

        crearVentanaPrincipal();
    }

    @SuppressWarnings("Convert2Lambda")
    public static void crearVentanaPrincipal() {
        SeguroVentana ventanaCrear = new SeguroVentana();
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

        botonCrear.addActionListener(new ActionListener() {

            @Override

            @SuppressWarnings("CallToPrintStackTrace")
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón CREAR presionado");
                // Aquí puedes agregar la lógica para crear un seguro

                // String username = inicioSesionVentana.emailInicioSesion; // Método para obtener el usuario

                // try {
                //     // Verificar si el usuario es administrador
                //     boolean esAdmin = client.verificarAdmin(username); // Llamada al servidor
                //     if (!esAdmin) {
                //         JOptionPane.showMessageDialog(null, "No tienes permisos para crear seguros.",
                //                 "Acceso denegado",
                //                 JOptionPane.ERROR_MESSAGE);
                //         return;
                //     }

                //     // Continuar con la funcionalidad normal
                     ventanaCrear.crearVentanaSeguro(false);
                // } catch (IOException ex) {
                //     // Manejar errores relacionados con la comunicación con el servidor
                //     JOptionPane.showMessageDialog(null,
                //             "Error de conexión con el servidor. Por favor, inténtelo más tarde.",
                //             "Error",
                //             JOptionPane.ERROR_MESSAGE);
                //     ex.printStackTrace(); // Registrar el error en los logs
                // } catch (IllegalArgumentException ex) {
                //     // Manejar errores específicos del servidor (como permisos denegados)
                //     JOptionPane.showMessageDialog(null, "Permiso denegado: " + ex.getMessage(),
                //             "Acceso denegado",
                //             JOptionPane.WARNING_MESSAGE);
                //     ex.printStackTrace(); // Registrar el error en los logs
                // } catch (RuntimeException ex) {
                //     // Manejar errores inesperados
                //     JOptionPane.showMessageDialog(null,
                //             "Ocurrió un error inesperado. Por favor, contacte al soporte.",
                //             "Error",
                //             JOptionPane.ERROR_MESSAGE);
                //     ex.printStackTrace(); // Registrar el error en los logs
                // } catch (InterruptedException ex) {
                //     JOptionPane.showMessageDialog(null,
                //             "La operación fue interrumpida. Por favor, inténtelo nuevamente.", "Error",
                //             JOptionPane.ERROR_MESSAGE);
                // }
            }
        });

        botonEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Botón EDITAR presionado");
                ventanaCrear.editarSeguro(textFieldSeguro.getText()); // Llama al método para editar un seguro
            }
        });

        panelInferior.add(botonCrear);
        panelInferior.add(botonEditar);

        // Añadir el panel inferior a la ventana
        ventana.add(panelInferior, BorderLayout.SOUTH);

        // Hacer visible la ventana
        ventana.setVisible(true);
    }
}
