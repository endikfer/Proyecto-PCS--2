package com.seguros.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seguros.Service.UsuarioService;
import com.seguros.model.Cliente;
import com.seguros.repository.ClienteRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;
    private final UsuarioService usuarioService;

    public ClienteController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Registrar nuevo cliente
    @PostMapping
    public ResponseEntity<?> createCliente(@Valid @RequestBody Cliente cliente) {
        try {
            if (clienteRepository.existsByEmail(cliente.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body("Error: El email ya está registrado");
            }

            if (cliente.getPassword().length() < 6) {
                return ResponseEntity
                        .badRequest()
                        .body("Error: La contraseña debe tener al menos 6 caracteres");
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

    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerClientePorEmail(@RequestParam String email) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
        }
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/esAdmin")
    public ResponseEntity<Boolean> esAdmin(@RequestParam String username) {
        boolean esAdmin = usuarioService.esAdmin(username);
        return ResponseEntity.ok(esAdmin);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Sesión de cliente cerrada correctamente");
    }
    
    @GetMapping("/cliente/porNombre")
    public ResponseEntity<Cliente> obtenerUsuarioPorNombre(@RequestParam String nombre) {
        Cliente cliente = usuarioService.obtenerClientePorNombre(nombre);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}