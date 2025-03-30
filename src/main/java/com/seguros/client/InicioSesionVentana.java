package com.seguros.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.seguros.model.Cliente;

public class InicioSesionVentana {
    private JFrame frame;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    private Cliente clienteLogueado;

    public String emailInicioSesion;

    // Configuración de la base de datos (debe coincidir con tu
    // application.properties)
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

        // Título
        JLabel lblTitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);

        // Label y campo para el Email
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        // Label y campo para la Contraseña
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Botón para iniciar sesión
        JButton btnLogin = new JButton("Iniciar Sesión");
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnLogin, gbc);

        btnLogin.addActionListener((ActionEvent e) -> {
            iniciarSesion();
        });

        frame.add(panel);
    }

    private void iniciarSesion() {
        String email = txtEmail.getText();
        emailInicioSesion = email;
        String password = new String(txtPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Todos los campos son obligatorios", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar credenciales consultando la base de datos
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
                        abrirVentanaPrincipal();
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

    private void abrirVentanaPrincipal() {
        // Se asume que SeguroManager tiene el método crearVentanaPrincipal()
        SeguroManager seguroManager = new SeguroManager();
        seguroManager.crearVentanaPrincipal();
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    // Método main para pruebas
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InicioSesionVentana().mostrar();
        });
    }

}
