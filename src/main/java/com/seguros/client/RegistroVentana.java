package com.seguros.client;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class RegistroVentana {
    private JFrame frame;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    // Configuración de la base de datos (debe coincidir con tu
    // application.properties)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/segurosdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public RegistroVentana() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Registro de Usuario");
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Componentes del formulario
        JLabel lblTitulo = new JLabel("Registro de Nuevo Usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField(20);

        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField(20);

        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField(20);

        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        // Organización de los componentes
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(lblNombre, gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(lblPassword, gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnRegistrar, gbc);

        frame.add(panel);
    }

    private void registrarUsuario() {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());

        if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Todos los campos son obligatorios",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(frame,
                    "La contraseña debe tener al menos 6 caracteres",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Verificar si el email ya existe
            if (emailExiste(conn, email)) {
                JOptionPane.showMessageDialog(frame,
                        "El email ya está registrado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insertar nuevo cliente
            String sql = "INSERT INTO clientes (nombre, email, password) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, nombre);
                pstmt.setString(2, email);
                pstmt.setString(3, password);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Registro exitoso!");
                frame.dispose(); // Cerrar ventana de registro
                abrirVentanaPrincipal(); // Abrir ventana principal
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Error al registrar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private boolean emailExiste(Connection conn, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private void abrirVentanaPrincipal() {
        SeguroManager seguroManager = new SeguroManager();
        seguroManager.crearVentanaPrincipal();
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}