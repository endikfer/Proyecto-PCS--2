package com.seguros.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seguros.Service.SeguroService;
import com.seguros.model.Seguro;

@RestController
@RequestMapping("/api/seguros")
public class SeguroController {

    private final SeguroService seguroService;

    public SeguroController(SeguroService seguroService) {
        this.seguroService = seguroService;
    }

    @PostMapping("/seguro/crear")
    public ResponseEntity<String> crearSeguro(
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipoSeguro") String tipoSeguro,
            @RequestParam("precio") Double precio) {
        try {
            System.out.println("Peticion recivida para crear seguro desde controller\n");

            if (nombre == null || nombre.isBlank() || descripcion == null || descripcion.isBlank() || precio == null
                    || precio <= 0) {
                return ResponseEntity.badRequest()
                        .body("Todos los campos son obligatorios y el precio debe ser mayor a 0.");
            }
            seguroService.crearSeguro(nombre, descripcion, tipoSeguro, precio);

            System.out.println("llegue4\n");

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            // Manejar la violación de unicidad
            return ResponseEntity.badRequest().body("El nombre del seguro ya existe. Por favor, elija otro nombre.");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/seguro/editar")
    public ResponseEntity<String> editarSeguro(
            @RequestParam("id") Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipoSeguro") String tipoSeguro,
            @RequestParam("precio") Double precio) {
        try {
            if (id == null || id <= 0 || nombre == null || nombre.isBlank() || descripcion == null || descripcion.isBlank()
                    || precio == null || precio <= 0) {
                return ResponseEntity.badRequest()
                        .body("Todos los campos son obligatorios y el precio debe ser mayor a 0.");
            }

            boolean actualizado = seguroService.editarSeguro(id, nombre, descripcion, tipoSeguro, precio);

            if (actualizado) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguro no encontrado.");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/seguro/obtenerPorNombre")
        public ResponseEntity<Seguro> obtenerSeguroPorNombre(@RequestParam("nombre") String nombre) {
        Seguro seguro = seguroService.obtenerSeguroPorNombre(nombre);
        if (seguro != null) {
            return ResponseEntity.ok(seguro);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}