package com.seguros.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import com.seguros.model.Factura;





public class FacturaControllerClient {
    private final HttpClient httpClient;
    private final String BASE_URL;
    private final ObjectMapper objectMapper = new ObjectMapper();
   

    // Constructor original
    public FacturaControllerClient(String hostname, String port) {
        this(HttpClient.newHttpClient(), hostname, port);
    }

    
    public FacturaControllerClient(HttpClient httpClient, String hostname, String port) {
        this.httpClient = httpClient;
        this.BASE_URL = String.format("http://%s:%s", hostname, port);
    }

    public String getBaseUrl() {
        return BASE_URL;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public List<Factura> getFacturasByClienteEmail(String email) throws IOException, InterruptedException {
        String uri = BASE_URL + "/api/facturas/cliente?gmail=" + email;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();

        if (statusCode == 200) {
            // Deserializar JSON a lista de facturas
            return objectMapper.readValue(response.body(), new TypeReference<List<Factura>>() {});
        } else if (statusCode == 204) {
            System.out.println("No hay facturas para este cliente.");
            return List.of();
        } else {
            System.err.println("Error al obtener facturas: " + response.body());
            return List.of();
        }

    
    }
    
    public void descargarFactura(Long clienteId, Path destino)
            throws IOException, InterruptedException {

        String uri = BASE_URL + "/api/facturas/" + clienteId + "/download";

        HttpRequest request = HttpRequest.newBuilder()
                                         .uri(URI.create(uri))
                                         .GET()
                                         .build();

        HttpResponse<InputStream> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() != 200) {
            throw new IOException("HTTP " + response.statusCode());
        }

        /* Solución: pasar la opción dentro de un array de CopyOption */
        CopyOption[] options = { StandardCopyOption.REPLACE_EXISTING };
        Files.copy(response.body(), destino, options);
    }
    
}
