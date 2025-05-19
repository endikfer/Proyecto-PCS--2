package com.seguros.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seguros.model.Factura;
import com.seguros.model.Seguro;
import com.seguros.repository.FacturaRepository;
import com.seguros.repository.SeguroRepository;

/**
 * @file FacturaService.java
 * @brief Servicio para la gestión de facturas.
 * 
 * Esta clase proporciona métodos para obtener facturas asociadas a clientes,
 * generar nuevas facturas y crear archivos de detalle de factura.
 * 
 * @author [Tu Nombre]
 * @date 2025-05-19
 */
@Service
public class FacturaService {
    /**
     * Repositorio para operaciones CRUD sobre facturas.
     */
    @Autowired
    private FacturaRepository facturaRepository;

    /**
     * Servicio para la gestión de seguros.
     */
    private final SeguroService seguroService;

    /**
     * Repositorio para operaciones sobre seguros.
     */
    private SeguroRepository segurorepo;
    
    /**
     * Constructor de FacturaService.
     * @param facturaRepository Repositorio de facturas.
     * @param seguroService Servicio de seguros.
     */
    public FacturaService(FacturaRepository facturaRepository,
            SeguroService seguroService) {
        this.facturaRepository = facturaRepository;
        this.seguroService    = seguroService;
        }    
    
    /**
     * Obtiene la lista de facturas asociadas a un cliente.
     * @param clienteId Identificador del cliente.
     * @return Lista de facturas del cliente.
     */
    public List<Factura> getFacturasByClienteId(Long clienteId) {
        return facturaRepository.findByIdCliente(clienteId);
    }
    
    /* ---------- NUEVA LÓGICA ---------- */
    /**
     * @brief Genera una factura para el cliente especificado, la persiste
     * y crea un archivo .txt con el detalle.
     *
     * @param clienteId id del cliente
     * @return Ruta absoluta del archivo de factura generado
     * @throws IOException si falla la escritura del fichero
     */
    public Path generarFacturaParaCliente(Long clienteId) throws IOException {

        /* 1) Seguros contratados por el cliente */
        List<Seguro> seguros = seguroService.obtenerSegurosPorCliente(clienteId);

        /* 2) Importe total (float) */
        float importeTotal = 0f;
        for (Seguro s : seguros) {
            importeTotal += s.getPrecio();          // getPrecio() → float
        }

        /* 3) Texto de la factura */
        String nombresSeguros = seguros.stream()
                                       .map(Seguro::getNombre)
                                       .collect(Collectors.joining(", "));
        String textoFactura = String.format(
            "Cliente %d, con seguros %s deberá abonar una cantidad total de %.2f €.",
            clienteId, nombresSeguros, importeTotal
        );

        /* 4) Construir y guardar el objeto Factura */
        Factura factura = new Factura();
        factura.setIdCliente(clienteId);
        factura.setCantidad(importeTotal);                  // float
        factura.setEstado("PENDIENTE");                     // String
        factura.setFecha(LocalDate.now()
                                  .format(DateTimeFormatter.ISO_DATE)); // String “2025-05-18”
        facturaRepository.save(factura);                    // persistencia JPA

        /* 5) Crear el archivo .txt */
        Path dir = Paths.get("facturas");
        Files.createDirectories(dir);
        Path path = dir.resolve("factura_" + factura.getId() + ".txt");
        Files.write(path, textoFactura.getBytes(StandardCharsets.UTF_8));

        /* 6) Devolver la ruta al controlador REST */
        return path.toAbsolutePath();
    }
    
}