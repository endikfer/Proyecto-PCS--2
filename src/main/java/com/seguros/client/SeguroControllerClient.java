package com.seguros.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SeguroControllerClient {
    private final HttpClient httpClient;
    private final String BASE_URL;

    public SeguroControllerClient(String hostname, String port) {
        this.httpClient = HttpClient.newHttpClient();
        this.BASE_URL = String.format("http://%s:%s", hostname, port);
    }

    public void crearSeguro(String nombre, String descripcion, String tipoSeguro, Double precio) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            BASE_URL + "/api/seguros/seguro/crear?nombre=" + nombre + "&descripcion=" + descripcion
                                    + "&tipoSeguro=" + tipoSeguro + "&precio=" + precio))
                    .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            switch (response.statusCode()) {
                case 200 -> {
                    System.out.println("Seguro creado correctamente.");
                }
                case 400 -> {
                    // Leer el mensaje de error enviado por el servidor
                    String errorMessage = response.body();
                    throw new IllegalArgumentException("Error al crear el seguro: " + errorMessage);
                }
                case 500 -> {
                    throw new RuntimeException("Error interno del servidor.");
                }
                default -> {
                    throw new RuntimeException(
                            "Fallo al crear el seguro con el código de estado: " + response.statusCode());
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error creando el seguro.", e);
        }
    }

    public void editarSeguro(Long id, String nombre, String descripcion, String tipoSeguro, Double precio) {
        try {
            String url = String.format(
                    BASE_URL + "/api/seguros/seguro/editar?id=%d&nombre=%s&descripcion=%s&tipoSeguro=%s&precio=%.2f",
                    id, nombre, descripcion, tipoSeguro, precio);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            switch (response.statusCode()) {
                case 200 -> {
                    System.out.println("Seguro editado correctamente");
                }
                case 400 -> throw new IllegalArgumentException("Solicitud incorrecta. Verifique los datos ingresados.");
                case 404 -> throw new RuntimeException("Seguro no encontrado.");
                case 500 -> throw new RuntimeException("Error interno del servidor.");
                default -> throw new RuntimeException(
                        "Fallo al editar el seguro con código de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error editando el seguro.", e);
        }
    }

}
