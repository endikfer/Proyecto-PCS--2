package com.seguros.client;

import java.net.http.HttpClient;



public class FacturaControllerClient {
    private final HttpClient httpClient;
    private final String BASE_URL;

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

    
    
}
