package com.seguros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.seguros.model.Administrador;
import com.seguros.repository.AdministradorRepository;

@RestController 
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdministradorRepository administradorRepository;

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
}
