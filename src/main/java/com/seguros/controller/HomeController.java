package com.seguros.controller;  // Mismo paquete que ClienteController

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "¡Bienvenido a la API de Seguros!";
    }
}