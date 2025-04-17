package com.seguros.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SeguroControllerAdmin {
    private final HttpClient httpClient;
    private final String BASE_URL;
    
    public SeguroControllerAdmin(String hostname, String port) {
        this.httpClient = HttpClient.newHttpClient();
        this.BASE_URL = String.format("http://%s:%s", hostname, port);
    }

    public List<String> listaNombreSeguros() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/seguros/seguro/obtenerTodos"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
    
            System.out.println("URL de la solicitud: " + request.uri()); // Imprimir la URL de la solicitud
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
            System.out.println("Código de estado de la respuesta: " + response.statusCode()); // Imprimir el código de estado
            switch (response.statusCode()) {
                case 200 -> {
                    ObjectMapper mapper = new ObjectMapper();
                    System.out.println("Respuesta del servidor: " + response.body()); // Imprimir el cuerpo de la respuesta
                    // Deserializamos el cuerpo de la respuesta en una lista de Strings
                    return mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
                }
                case 404 -> throw new RuntimeException("No se encontraron seguros en el servidor.");
                default -> throw new RuntimeException("Error al obtener los seguros: Código de estado " + response.statusCode());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error de entrada/salida al obtener los seguros.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
            throw new RuntimeException("La solicitud fue interrumpida.", e);
        }
    }
    
}
