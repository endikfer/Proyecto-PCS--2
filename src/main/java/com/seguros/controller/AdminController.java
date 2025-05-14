package com.seguros.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seguros.Service.AdminService;
import com.seguros.model.Administrador;
import com.seguros.repository.AdministradorRepository;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminService adminService;
    private final AdministradorRepository administradorRepository;

    // Constructor para inyección de dependencias
    public AdminController(AdminService adminService, AdministradorRepository administradorRepository) {
        this.adminService = adminService;
        this.administradorRepository = administradorRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Administrador admin) {
        Administrador administrador = administradorRepository.findByEmail(admin.getEmail());
        if (administrador != null && administrador.getPassword().equals(admin.getPassword())) {
            // Autenticación exitosa
            return ResponseEntity.ok("Inicio de sesión exitoso");
        } else {
            // Autenticación fallida
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Aquí podrías invalidar tokens, sesiones, etc. si tuvieras.
        return ResponseEntity.ok("Sesión cerrada exitosamente.");
    }

    @GetMapping("/duda/obtenerTodosAsuntos")
    public ResponseEntity<?> obtenerTodosAsuntos() {
        try {
            System.out.println("Petición recibida para obtener todos los asuntos desde el controller\n");
            List<String> asuntos = adminService.getAllAsuntoDudas();

            if (asuntos == null || asuntos.isEmpty()) {
                System.out.println("No se encontraron asuntos en la base de datos.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontraron asuntos.");
            }

            System.out.println("Asuntos obtenidos: " + asuntos.size());
            return new ResponseEntity<>(asuntos, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error al obtener todos los asuntos: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/duda/obtenerMensajeByAsunto")
    public ResponseEntity<String> obtenerMensajeByAsunto(@RequestParam String asunto) {
        try {
            System.out.println("Petición recibida para obtener el mensaje del asuntos desde el controller\n");
            System.out.println("Asunto recibido: " + asunto + "\n");
            String mensaje = adminService.getMensajeByAsuntoDudas(asunto);

            if (mensaje == null || mensaje.trim().isEmpty()) {
                System.out.println("No se encontró un mensaje para el asunto proporcionado.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontró un mensaje para el asunto proporcionado.");
            }

            return new ResponseEntity<>(mensaje, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error al obtener el mensaje del asuntos: " + asunto+ "con mensaje de error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
