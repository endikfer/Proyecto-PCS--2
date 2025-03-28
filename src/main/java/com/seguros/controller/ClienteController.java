package com.seguros.controller;

import com.seguros.model.Cliente;
import com.seguros.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // Registrar nuevo cliente
    @PostMapping
    public ResponseEntity<?> createCliente(@Valid @RequestBody Cliente cliente) {
        try {
            if (clienteRepository.existsByEmail(cliente.getEmail())) {
                return ResponseEntity
                    .badRequest()
                    .body("Error: El email ya está registrado");
            }


            cliente.setPassword(cliente.getPassword());
            // Guarda la contraseña en texto plano
            Cliente clienteGuardado = clienteRepository.save(cliente);

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteGuardado);
            
        } catch (Exception e) {
            return ResponseEntity
                .internalServerError()
                .body("Error al registrar: " + e.getMessage());
        }
    }

    // Obtener todos los clientes (mostrará contraseñas en texto plano)
    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    // Obtener cliente por ID (mostrará contraseña en texto plano)
    @GetMapping("/{id}")
    public Optional<Cliente> getClienteById(@PathVariable Long id) {
        return clienteRepository.findById(id);
    }
}