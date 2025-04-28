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

@SpringBootTest(classes = com.seguros.SegurosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeguroCasaIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testClienteContrataYAnulaSeguroCasa() {
        // Paso 1: Crear cliente (POST, esperando String)
        Cliente cliente = new Cliente(
                "Juan Pérez",
                "juan" + System.currentTimeMillis() + "@email.com",
                "123456");

        HttpHeaders clienteHeaders = new HttpHeaders();
        clienteHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Cliente> clienteRequest = new HttpEntity<>(cliente, clienteHeaders);

        ResponseEntity<String> clienteResponse = restTemplate.postForEntity(
                "/api/clientes",
                clienteRequest,
                String.class);

        assertEquals(HttpStatus.CREATED, clienteResponse.getStatusCode());

        // Paso 2: Crear un seguro
        String nombre = "Alvaro" + System.currentTimeMillis();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("nombre", nombre);
        params.add("descripcion", "seguro nuevo");
        params.add("tipoSeguro", "CASA");
        params.add("precio", "2500.0");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> seguroResponse = restTemplate.postForEntity(
                "/api/seguros/seguro/crear",
                request,
                String.class);

        assertEquals(HttpStatus.OK, seguroResponse.getStatusCode());

        // Paso 3: Obtener el seguro creado por nombre
        
        
        ResponseEntity<Seguro> seguroObtenidoResponse = restTemplate.getForEntity(
                "/api/seguros/seguro/obtenerPorNombre?nombre=" + nombre,
                Seguro.class
        );

        assertEquals(HttpStatus.OK, seguroObtenidoResponse.getStatusCode());
        Seguro seguroCreado = seguroObtenidoResponse.getBody();
        assertNotNull(seguroCreado, "El seguro creado no debe ser nulo");

        Long seguroId = seguroCreado.getId();
        assertNotNull(seguroId, "El ID del seguro no debe ser nulo");
        System.out.println("ID del seguro creado: " + seguroId);

        // Paso 4: Editar el seguro creado
        MultiValueMap<String, String> editParams = new LinkedMultiValueMap<>();
        editParams.add("id", String.valueOf(seguroId));
        editParams.add("nombre", "Alvaro Editado" + System.currentTimeMillis());
        editParams.add("descripcion", "seguro actualizado");
        editParams.add("tipoSeguro", "CASA");
        editParams.add("precio", "3000.0");

        HttpEntity<MultiValueMap<String, String>> editRequest = new HttpEntity<>(editParams, headers);

        ResponseEntity<String> editResponse = restTemplate.postForEntity(
            "/api/seguros/seguro/editar",
            editRequest,
            String.class
        );

        assertEquals(HttpStatus.OK, editResponse.getStatusCode());
        System.out.println("Respuesta al editar seguro: " + editResponse.getBody());
    }
}