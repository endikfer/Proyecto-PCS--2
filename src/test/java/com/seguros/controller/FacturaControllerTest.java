package com.seguros.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seguros.Service.FacturaService;
import com.seguros.model.Cliente;
import com.seguros.model.Factura;
import com.seguros.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacturaController.class)
public class FacturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacturaService facturaService;

    @MockBean
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetFacturasByClienteEmail_ClienteConFacturas() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        List<Factura> facturas = Arrays.asList(new Factura(), new Factura());

        when(clienteRepository.findByEmail("cliente@gmail.com")).thenReturn(cliente);
        when(facturaService.getFacturasByClienteId(1L)).thenReturn(facturas);

        mockMvc.perform(get("/api/facturas/cliente")
                        .param("gmail", "cliente@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetFacturasByClienteEmail_ClienteSinFacturas() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        when(clienteRepository.findByEmail("cliente@gmail.com")).thenReturn(cliente);
        when(facturaService.getFacturasByClienteId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/facturas/cliente")
                        .param("gmail", "cliente@gmail.com"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No se encontraron facturas para este cliente."));
    }

    @Test
    void testGetFacturasByClienteEmail_ClienteNoExiste() throws Exception {
        when(clienteRepository.findByEmail("desconocido@gmail.com")).thenReturn(null);

        mockMvc.perform(get("/api/facturas/cliente")
                        .param("gmail", "desconocido@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado con el email proporcionado."));
    }

    @Test
    void testGetFacturasByClienteEmail_ExcepcionInterna() throws Exception {
        when(clienteRepository.findByEmail("error@gmail.com")).thenThrow(new RuntimeException("DB crash"));

        mockMvc.perform(get("/api/facturas/cliente")
                        .param("gmail", "error@gmail.com"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al obtener las facturas."));
    }
    
}
