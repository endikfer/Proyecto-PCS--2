package com.seguros.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.seguros.model.Cliente;
import com.seguros.model.Seguro;

public class SeguroControllerClient {
    private final HttpClient httpClient;
    private final String BASE_URL;

    // Constructor original
    public SeguroControllerClient(String hostname, String port) {
        this(HttpClient.newHttpClient(), hostname, port);
    }

    public SeguroControllerClient(HttpClient httpClient, String hostname, String port) {
        this.httpClient = httpClient;
        this.BASE_URL = String.format("http://%s:%s", hostname, port);
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void enviarDuda(String asunto, String mensaje) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> body = new HashMap<>();
            body.put("asunto", asunto);
            body.put("mensaje", mensaje);
    
            String json = mapper.writeValueAsString(body);
    
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/clientes/duda")) // o /api/dudas si usas DudaController
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
    
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    
            if (response.statusCode() != 200) {
                throw new RuntimeException("Error al enviar duda. Código: " + response.statusCode());
            }
    
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error al enviar duda.", e);
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
                        new TypeReference<List<Seguro>>() {
                        });
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
                        new TypeReference<List<Seguro>>() {
                        });
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

    public List<Seguro> obtenerSegurosPorCliente(Long clienteId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/seguros/porCliente?clienteId=" + clienteId))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(response.body(), new TypeReference<List<Seguro>>() {});
            } else if (response.statusCode() == 204) {
                return Collections.emptyList();
            } else {
                throw new RuntimeException("Error al obtener seguros del cliente: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error obteniendo seguros del cliente.", e);
        }
    }

    @PostMapping("/duda")
    public ResponseEntity<String> enviarDuda(@RequestBody Map<String, String> datos) {
        String mensaje = datos.get("mensaje");
        
        System.out.println("Contenido: " + mensaje);
        
        return ResponseEntity.ok("Duda recibida correctamente.");
    
    }

    @PostMapping("/guardarVida")
    public ResponseEntity<String> contratarSeguroVida(
            @RequestParam Long clienteId,
            @RequestParam Long seguroId,
            @RequestParam int edad,
            @RequestParam String beneficiarios) {
        try {
            if (edad <= 0 || beneficiarios == null || beneficiarios.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Edad y beneficiarios son obligatorios y válidos.");
            }

            // Aquí iría la lógica para guardar el seguro de vida en la base de datos.
            // Por ejemplo: seguroService.contratarSeguroVida(clienteId, edad, beneficiarios);

            return ResponseEntity.ok("Seguro de vida contratado exitosamente para el cliente " + clienteId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al contratar el seguro de vida: " + e.getMessage());
        }
    }

    @PostMapping("/guardarCoche")
public ResponseEntity<String> contratarSeguroCoche(
        @RequestParam Long clienteId,
        @RequestParam Long seguroId,
        @RequestParam String matricula,
        @RequestParam String modelo,
        @RequestParam String marca) {
    try {
        if (matricula == null || matricula.trim().isEmpty() ||
            modelo == null || modelo.trim().isEmpty() ||
            marca == null || marca.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Matrícula, modelo y marca son obligatorios y válidos.");
        }

        // Aquí iría la lógica para guardar el seguro de coche en la base de datos.
        // Por ejemplo: seguroService.contratarSeguroCoche(clienteId, seguroId, matricula, modelo, marca);

        return ResponseEntity.ok("Seguro de coche contratado exitosamente para el cliente " + clienteId);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al contratar el seguro de coche: " + e.getMessage());
    }
}

@PostMapping("/guardarCasa")
public ResponseEntity<String> contratarSeguroCasa(
        @RequestParam Long clienteId,
        @RequestParam Long seguroId,
        @RequestParam String direccion,
        @RequestParam double valorInmueble,
        @RequestParam String tipoVivienda) {
    try {
        if (direccion == null || direccion.trim().isEmpty() ||
            tipoVivienda == null || tipoVivienda.trim().isEmpty() ||
            valorInmueble <= 0) {
            return ResponseEntity.badRequest().body("Dirección, tipo de vivienda y valor del inmueble válidos son obligatorios.");
        }

        // Aquí iría la lógica para guardar el seguro de casa en la base de datos.
        // Por ejemplo: seguroService.contratarSeguroCasa(clienteId, seguroId, direccion, valorInmueble, tipoVivienda);

        return ResponseEntity.ok("Seguro de casa contratado exitosamente para el cliente " + clienteId);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al contratar el seguro de casa: " + e.getMessage());
    }
}

}
