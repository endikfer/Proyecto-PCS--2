package com.seguros.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import javax.swing.JOptionPane;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seguros.model.Seguro;

public class SeguroControllerAdmin {
    private final HttpClient httpClient;
    private final String BASE_URL;

    // Constructor original
    public SeguroControllerAdmin(String hostname, String port) {
        this(HttpClient.newHttpClient(), hostname, port);
    }

    public SeguroControllerAdmin(HttpClient httpClient, String hostname, String port) {
        this.httpClient = httpClient;
        this.BASE_URL = String.format("http://%s:%s", hostname, port);
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public List<Seguro> listaNombreSeguros() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/seguros/seguro/obtenerTodos"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            System.out.println("URL de la solicitud: " + request.uri()); // Imprimir la URL de la solicitud
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Código de estado de la respuesta: " + response.statusCode()); // Imprimir el código de
                                                                                              // estado
            switch (response.statusCode()) {
                case 200 -> {
                    ObjectMapper mapper = new ObjectMapper();
                    System.out.println("Respuesta del servidor: " + response.body()); // Imprimir el cuerpo de la
                                                                                      // respuesta
                    // Deserializamos el cuerpo de la respuesta en una lista de objetos Seguro
                    return mapper.readValue(response.body(),
                            mapper.getTypeFactory().constructCollectionType(List.class, Seguro.class));
                }
                case 404 -> throw new RuntimeException("No se encontraron seguros en el servidor.");
                default -> throw new RuntimeException(
                        "Error al obtener los seguros: Código de estado " + response.statusCode());
            }
        } catch (IOException e) {
            throw new RuntimeException("Error de entrada/salida al obtener los seguros.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurar el estado de interrupción
            throw new RuntimeException("La solicitud fue interrumpida.", e);
        }
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
                    JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
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
            System.out.println("Código de estado de la respuesta: " + response.statusCode()); // Imprimir el código de
                                                                                              // estado
            switch (response.statusCode()) {
                case 200 -> {
                    System.out.println("Seguro editado correctamente");
                }
                case 400 -> throw new IllegalArgumentException("Solicitud incorrecta. Verifique los datos ingresados.");
                case 404 -> throw new RuntimeException("Seguro no encontrado.");
                case 409 -> {
                    String mensaje = "Seguro existente con el mismo nombre. Por favor, elija otro nombre.";
                    // JOptionPane.showMessageDialog(null, mensaje, "Conflicto",
                    // JOptionPane.WARNING_MESSAGE);
                    throw new RuntimeException(mensaje);
                }
                case 500 -> throw new RuntimeException("Error interno del servidor.");
                default -> throw new RuntimeException(
                        "Fallo al editar el seguro con código de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error editando el seguro.", e);
        }
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

            System.out.println("Código de estado de la respuesta: " + response.statusCode()); // Imprimir el código de
                                                                                              // estado

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                System.out.println("Respuesta del servidor: " + response.body()); // Imprimir el cuerpo de la respuesta
                // Deserializamos el cuerpo de la respuesta en un objeto Seguro
                return mapper.readValue(response.body(), Seguro.class); // Cambiar response.toString() por
                                                                        // response.body()
            } else if (response.statusCode() == 404) {
                throw new RuntimeException("Seguro no encontrado.");
            } else {
                throw new RuntimeException("Error al obtener el seguro: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error obteniendo el seguro.", e);
        }
    }

    public void eliminarSeguro(Long id) {
        try {
            String url = BASE_URL + "/api/seguros/seguro/eliminar?id=" + id;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .DELETE()
                    .build();

            System.out.println("URL de la solicitud: " + request.uri());
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            System.out.println("Código de estado de la respuesta: " + response.statusCode());

            switch (response.statusCode()) {
                case 200 -> System.out.println("Seguro eliminado correctamente.");
                case 400 -> throw new IllegalArgumentException("ID inválido.");
                case 404 -> throw new RuntimeException("Seguro no encontrado.");
                case 500 -> throw new RuntimeException("Error interno del servidor.");
                default -> throw new RuntimeException(
                        "Fallo al eliminar el seguro con código de estado: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error eliminando el seguro.", e);
        }
    }

    public List<String> obtenerTodosAsuntos() {
        try {
            System.out.println("Pedir request\n");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/admin/duda/obtenerTodosAsuntos"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            System.out.println("URL de la solicitud: " + request.uri()); // Imprimir la URL de la solicitud
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Request pedida\n");

            System.out.println("Código de estado de la respuesta: " + response.statusCode()); // Imprimir el código de
                                                                                              // estado

            if (response.statusCode() == 200) {
                // Deserializar la lista de strings
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(),
                        mapper.getTypeFactory().constructCollectionType(List.class, String.class));
            } else if (response.statusCode() == 204) {
                // No hay contenido, devolver una lista vacía
                System.out.println("No se encontraron asuntos en el servidor.");
                return List.of();
            } else {
                // Manejar otros errores
                throw new RuntimeException("Error al obtener los asuntos: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error obteniendo los asuntos.", e);
        }
    }

}
