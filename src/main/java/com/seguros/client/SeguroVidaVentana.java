package com.seguros.client;

import com.seguros.model.Seguro;
import javax.swing.*;
import java.awt.*;

public class SeguroVidaVentana {
    private JFrame frame;
    private JTextField txtEdad;
    private JTextField txtBeneficiarios;
    private Seguro seguro;
    SeguroControllerClient client = new SeguroControllerClient("localhost", "8080");

    public SeguroVidaVentana(Seguro seguro) {
        this.seguro = seguro;
        initialize();
    }

    public void setSeguro(Seguro seguro) {
        this.seguro = seguro;
    }

    void initialize() {
        frame = new JFrame("Seguro de Vida - " + seguro.getNombre());
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Edad del Asegurado:"));
        txtEdad = new JTextField();
        panel.add(txtEdad);

        panel.add(new JLabel("Beneficiarios:"));
        txtBeneficiarios = new JTextField();
        panel.add(txtBeneficiarios);

        JButton btnContratar = new JButton("Contratar Seguro");
        btnContratar.addActionListener(e -> contratarSeguro());
        panel.add(btnContratar);

        frame.add(panel);
    }

    private void contratarSeguro() {
        String edadTexto = txtEdad.getText().trim();
        String beneficiarios = txtBeneficiarios.getText().trim();

        if (edadTexto.isEmpty() || beneficiarios.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Todos los campos son obligatorios.",
                    "Campos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int edad = Integer.parseInt(edadTexto);
            if (edad <= 0) {
                throw new NumberFormatException("La edad debe ser un número positivo");
            }

            // Llamada al SeguroControllerClient
            SeguroControllerClient client = new SeguroControllerClient("localhost", "8080");
            // Aquí debes obtener el clienteId real según tu lógica de aplicación
            Long clienteId = 1L; // Ejemplo, reemplaza por el id real del cliente
            Long seguroId = seguro.getId(); // Suponiendo que Seguro tiene getId()

            // Realizar la llamada al endpoint REST
            String url = client.getBaseUrl() + "/guardarVida"
                    + "?clienteId=" + clienteId
                    + "&seguroId=" + seguroId
                    + "&edad=" + edad
                    + "&beneficiarios=" + beneficiarios;

            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url))
                    .POST(java.net.http.HttpRequest.BodyPublishers.noBody())
                    .build();

            java.net.http.HttpResponse<String> response = client.getHttpClient()
                    .send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JOptionPane.showMessageDialog(frame,
                        "Seguro de vida contratado exitosamente!\n" +
                                "Seguro: " + seguro.getNombre() + "\n" +
                                "Edad: " + edad + "\n" +
                                "Beneficiarios: " + beneficiarios);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Error al contratar el seguro: " + response.body(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame,
                    "La edad debe ser un número válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame,
                    "Error al contratar el seguro: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mostrar() {
        frame.setVisible(true);
    }
}