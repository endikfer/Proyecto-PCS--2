package com.seguros.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.seguros.model.Cliente;
import com.seguros.model.Seguro;

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
            String url = BASE_URL + "/api/seguros/seguro/editar";
HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .header("Content-Type", "application/x-www-form-urlencoded") // Cambia el tipo de contenido
        .POST(HttpRequest.BodyPublishers.ofString(
                "id=" + id + "&nombre=" + nombre.trim() + "&descripcion=" + descripcion.trim() + 
                "&tipoSeguro=" + tipoSeguro + "&precio=" + precio))
        .build();
            System.out.println("URL de la solicitud: " + request.uri()); // Imprimir la URL de la solicitud
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            System.out.println("Código de estado de la respuesta: " + response.statusCode()); // Imprimir el código de estado
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

    public boolean verificarAdmin(String username) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/usuarios/esAdmin?username=" + username))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return Boolean.parseBoolean(response.body());
    }

    public Seguro obtenerSeguroPorNombre(String nombre) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/seguros/seguro/obtenerPorNombre?nombre=" + nombre))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
    
            System.out.println("URL de la solicitud: " + request.uri()); // Imprimir la URL de la solicitud
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Código de estado de la respuesta: " + response.statusCode()); // Imprimir el código de estado
    
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                System.out.println("Respuesta del servidor: " + response.body()); // Imprimir el cuerpo de la respuesta
            // Deserializamos el cuerpo de la respuesta en un objeto Seguro
            return mapper.readValue(response.body(), Seguro.class); // Cambiar response.toString() por response.body()
            } else if (response.statusCode() == 404) {
                throw new RuntimeException("Seguro no encontrado.");
            } else {
                throw new RuntimeException("Error al obtener el seguro: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error obteniendo el seguro.", e);
        }
    }

    public List<Seguro> obtenerTodosSeguros() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/seguros/seguro/obtenerTodos"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
    
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), 
                    new TypeReference<List<Seguro>>(){});
            } else {
                throw new RuntimeException("Error al obtener seguros: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error obteniendo seguros.", e);
        }
    }
    
    public List<Seguro> obtenerSegurosPorTipo(String tipoSeguro) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/seguros/seguro/obtenerPorTipo?tipoSeguro=" + tipoSeguro))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
    
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), 
                    new TypeReference<List<Seguro>>(){});
            } else {
                throw new RuntimeException("Error al obtener seguros por tipo: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error obteniendo seguros por tipo.", e);
        }
    }

    public Cliente obtenerDatosCliente(String email) {
    try {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/clientes/perfil?email=" + email))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.body(), Cliente.class);
        } else if (response.statusCode() == 404) {
            throw new RuntimeException("Cliente no encontrado.");
        } else {
            throw new RuntimeException("Error al obtener datos del cliente: " + response.statusCode());
        }
    } catch (IOException | InterruptedException e) {
        throw new RuntimeException("Error obteniendo datos del cliente.", e);
    }
}

}
