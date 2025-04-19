package com.seguros.client;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

import com.seguros.model.Cliente;

public class InicioSesionVentana {
    private JFrame frame;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<String> rolCombo;

    private Cliente clienteLogueado;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/segurosdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public InicioSesionVentana() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Inicio de Sesión");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Tipo de usuario:"), gbc);

        rolCombo = new JComboBox<>(new String[] { "Cliente", "Administrador" });
        gbc.gridx = 1;
        panel.add(rolCombo, gbc);

        JButton btnLogin = new JButton("Iniciar Sesión");
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener((ActionEvent e) -> {
            iniciarSesion();
        });

        JButton btnRegister = new JButton("Registrarse");
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(btnRegister, gbc);

        btnRegister.addActionListener((ActionEvent e) -> {
            frame.dispose();
            RegistroVentana registroVentana = new RegistroVentana();
            registroVentana.mostrar();
        });

        frame.add(panel);
    }

    private void iniciarSesion() {
        String email = txtEmail.getText();
        String password = new String(txtPassword.getPassword());
        String rolSeleccionado = (String) rolCombo.getSelectedItem();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Todos los campos son obligatorios", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ("Administrador".equals(rolSeleccionado)) {
            if (email.equals("admin@gmail.com") && password.equals("1234")) {
                JOptionPane.showMessageDialog(frame, "Inicio de sesión como administrador exitoso!");
                frame.dispose();
                abrirVentanaAdmin();
                return;
            } else {
                JOptionPane.showMessageDialog(frame, "Usuario o contraseña de administrador incorrectos", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM clientes WHERE email = ? AND password = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setString(2, password);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Long id = rs.getLong("id");
                        String nombre = rs.getString("nombre");
                        String emailDB = rs.getString("email");
                        String passwordDB = rs.getString("password");

                        clienteLogueado = new Cliente(nombre, emailDB, passwordDB);

                        JOptionPane.showMessageDialog(frame, "Inicio de sesión exitoso!");
                        frame.dispose();
                        abrirVentanaPrincipal(clienteLogueado);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Email o contraseña incorrectos", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error de conexión: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVentanaPrincipal(Cliente cliente) {
        SeguroManager seguroManager = new SeguroManager();
        seguroManager.crearVentanaPrincipal(cliente);
    }

    private void abrirVentanaAdmin() {
        AdminVentana adminVentana = new AdminVentana();
        adminVentana.mostrar();
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InicioSesionVentana().mostrar();
        });
    }
} 