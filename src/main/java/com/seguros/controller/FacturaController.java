package com.seguros.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seguros.Service.FacturaService;
import com.seguros.model.Cliente;
import com.seguros.model.Factura;
import com.seguros.repository.ClienteRepository;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {
    private final FacturaService facturaService;
    private final ClienteRepository clienteRepository;

    public FacturaController(FacturaService facturaService, ClienteRepository clienteRepository) {
        this.facturaService = facturaService;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/cliente")
    public ResponseEntity<?> getFacturasByClienteEmail(@RequestParam String gmail) {
        try {
            // Buscar cliente por email
            Cliente cliente = clienteRepository.findByEmail(gmail);

            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado con el email proporcionado.");
            }

            // Obtener facturas usando su ID
            List<Factura> facturas = facturaService.getFacturasByClienteId(cliente.getId());

            if (facturas == null || facturas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontraron facturas para este cliente.");
            }

            return ResponseEntity.ok(facturas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las facturas.");
        }
    }
    
}
