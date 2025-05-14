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
import com.seguros.model.Factura;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {
    
    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping("/cliente")
    public ResponseEntity<?> getFacturasByClienteId(@RequestParam Long clienteId) {
        try {
            List<Factura> facturas = facturaService.getFacturasByClienteId(clienteId);
            if (facturas == null || facturas.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontraron facturas para este cliente.");
            }
            return ResponseEntity.ok(facturas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener las facturas.");
        }
    }
    
}
