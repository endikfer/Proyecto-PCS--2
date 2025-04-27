package com.integration;

import com.seguros.model.Cliente;
import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest(classes = com.seguros.SegurosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeguroCasaIntegrationTest {

    // @Autowired
    // private TestRestTemplate restTemplate;

    // @Test
    // void testClienteContrataYAnulaSeguroCasa() {
    // // Paso 1: Crear cliente (POST, esperando String)
    // Cliente cliente = new Cliente(
    // "Juan Pérez",
    // "juan" + System.currentTimeMillis() + "@email.com",
    // "123456"
    // );

    // HttpHeaders clienteHeaders = new HttpHeaders();
    // clienteHeaders.setContentType(MediaType.APPLICATION_JSON);

    // HttpEntity<Cliente> clienteRequest = new HttpEntity<>(cliente,
    // clienteHeaders);

    // ResponseEntity<String> clienteResponse = restTemplate.postForEntity(
    // "/api/clientes",
    // clienteRequest,
    // String.class
    // );

    // assertEquals(HttpStatus.CREATED, clienteResponse.getStatusCode());

    // //Paso 2: Crear un seguro
    // MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    // params.add("nombre", "Alvaro");
    // params.add("descripcion", "seguro nuevo");
    // params.add("tipoSeguro", "CASA");
    // params.add("precio", "2500.0");

    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    // HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params,
    // headers);

    // ResponseEntity<String> seguroResponse = restTemplate.postForEntity(
    // "/api/seguros/seguro/crear",
    // request,
    // String.class
    // );

    // assertEquals(HttpStatus.OK, seguroResponse.getStatusCode());
    // }
}