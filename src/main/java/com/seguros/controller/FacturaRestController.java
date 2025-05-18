package com.seguros.controller;

import com.seguros.Service.FacturaService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin      // permite llamadas desde la UI Swing vía HTTP
public class FacturaRestController {

    private final FacturaService facturaService;

    public FacturaRestController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    /**
     * Genera la factura del cliente y devuelve el fichero .txt como descarga.
     *
     * Ejemplo: GET /api/facturas/7/download
     */
    @GetMapping("/{clienteId}/download")
    public ResponseEntity<Resource> download(@PathVariable Long clienteId) throws IOException {

        /* 1) Generar factura y obtener la ruta */
        Path path = facturaService.generarFacturaParaCliente(clienteId);

        /* 2) Convertir a recurso HTTP */
        byte[] bytes = Files.readAllBytes(path);
        Resource resource = new ByteArrayResource(bytes);

        /* 3) Cabeceras respuesta */
        String fileName = path.getFileName().toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(bytes.length)
                .body(resource);
    }
}
