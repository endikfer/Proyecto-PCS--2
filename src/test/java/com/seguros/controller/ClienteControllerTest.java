package com.seguros.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seguros.Service.UsuarioService;
import com.seguros.model.Cliente;
import com.seguros.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateCliente_OK() throws Exception {
        Cliente cliente = new Cliente("user01", "user01@gmail.com", "123456");

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user01@gmail.com"));
    }

    @Test
    void testCreateCliente_EmailExiste() throws Exception {
        Cliente cliente = new Cliente("user01", "user01@gmail.com", "123456");

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(true);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: El email ya está registrado"));
    }

    @Test
    void testCreateCliente_Excepcion() throws Exception {
        Cliente cliente = new Cliente("user01", "user01@gmail.com", "123456");

        when(clienteRepository.existsByEmail(cliente.getEmail())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenThrow(new RuntimeException("Fallo DB"));

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error al registrar")));
    }

    @Test
    void testGetAllClientes() throws Exception {
        Cliente cliente1 = new Cliente("user02", "user02@gmail.com", "123456");
        Cliente cliente2 = new Cliente("user03", "user03@gmail.com", "abcdef");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetClienteById() throws Exception {
        Cliente cliente = new Cliente("user04", "user04@gmail.com", "abcdef");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user04@gmail.com"));
    }

    @Test
    void testObtenerClientePorEmail_Existe() throws Exception {
        Cliente cliente = new Cliente("user05", "user05@gmail.com", "password");

        when(clienteRepository.findByEmail("user05@gmail.com")).thenReturn(cliente);

        mockMvc.perform(get("/api/clientes/perfil")
                        .param("email", "user05@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("user05"));
    }

    @Test
    void testObtenerClientePorEmail_NoExiste() throws Exception {
        when(clienteRepository.findByEmail("noexiste@gmail.com")).thenReturn(null);

        mockMvc.perform(get("/api/clientes/perfil")
                        .param("email", "noexiste@gmail.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado"));
    }

    @Test
    void testEsAdmin() throws Exception {
        when(usuarioService.esAdmin("admin@gmail.com")).thenReturn(true);

        mockMvc.perform(get("/api/clientes/esAdmin")
                        .param("username", "admin@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/clientes/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Sesión de cliente cerrada correctamente"));
    }

    @Test
    void testEliminarCliente_Existe() throws Exception {
        Cliente cliente = new Cliente("user06", "user06@gmail.com", "123456");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente eliminado correctamente"));
    }

    @Test
    void testEliminarCliente_NoExiste() throws Exception {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cliente no encontrado"));
    }

}
