package com.seguros.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seguros.Service.ClientesPorSeguro;
import com.seguros.Service.SeguroService;
import com.seguros.model.Cliente;
import com.seguros.model.Seguro;
import com.seguros.model.SeguroCasa;
import com.seguros.model.SeguroCoche;
import com.seguros.model.SeguroVida;

/**
 * @class SeguroController
 * @brief Controlador REST para la gestión de seguros.
 *
 *        Proporciona endpoints para crear, editar, eliminar, consultar y
 *        asociar seguros a clientes.
 */
@RestController
@RequestMapping("/api/seguros")
public class SeguroController {

    /** Servicio para la gestión de seguros. */
    private final SeguroService seguroService;

    /**
     * Constructor para inyección de dependencias.
     * 
     * @param seguroService Servicio de seguros.
     */
    public SeguroController(SeguroService seguroService) {
        this.seguroService = seguroService;
    }

    /**
     * Endpoint para crear un nuevo seguro.
     * 
     * @param nombre      Nombre del seguro.
     * @param descripcion Descripción del seguro.
     * @param tipoSeguro  Tipo de seguro.
     * @param precio      Precio del seguro.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
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

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            // Manejar la violación de unicidad
            return ResponseEntity.badRequest().body("El nombre del seguro ya existe. Por favor, elija otro nombre.");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para editar un seguro existente.
     * 
     * @param id          ID del seguro.
     * @param nombre      Nuevo nombre.
     * @param descripcion Nueva descripción.
     * @param tipoSeguro  Nuevo tipo de seguro.
     * @param precio      Nuevo precio.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @PostMapping("/seguro/editar")
    public ResponseEntity<String> editarSeguro(
            @RequestParam("id") Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("tipoSeguro") String tipoSeguro,
            @RequestParam("precio") Double precio) {
        try {
            if (id == null || id <= 0 || nombre == null || nombre.isBlank() || descripcion == null
                    || descripcion.isBlank()
                    || precio == null || precio <= 0) {
                return ResponseEntity.badRequest()
                        .body("Todos los campos son obligatorios y el precio debe ser mayor a 0.");
            }
            boolean actualizado = seguroService.editarSeguro(id, nombre.trim(), descripcion.trim(), tipoSeguro, precio);
            if (actualizado) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguro no encontrado.");
            }
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            // Manejar la violación de unicidad
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Seguro existente con el mismo nombre. Por favor, elija otro nombre.");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener un seguro por su nombre.
     * 
     * @param nombre Nombre del seguro.
     * @return Seguro correspondiente al nombre o NOT_FOUND si no existe.
     */
    @GetMapping("/seguro/obtenerPorNombre")
    public ResponseEntity<Seguro> obtenerSeguroPorNombre(@RequestParam("nombre") String nombre) {
        Seguro seguro = seguroService.obtenerSeguroPorNombre(nombre);
        if (seguro != null) {
            return new ResponseEntity<>(seguro, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Endpoint para eliminar un seguro por su ID.
     * 
     * @param id ID del seguro.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @DeleteMapping("/seguro/eliminar")
    public ResponseEntity<String> eliminarSeguro(@RequestParam("id") Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("ID inválido.");
            }
            boolean eliminado = seguroService.eliminarSeguro(id);
            if (eliminado) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguro no encontrado.");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener todos los seguros almacenados.
     * 
     * @return Lista de seguros o un seguro vacío si no hay resultados.
     */
    @GetMapping("/seguro/obtenerTodos")
    public ResponseEntity<List<Seguro>> obtenerTodosSeguros() {
        try {
            System.out.println("Petición recibida para obtener todos los seguros desde el controller\n");
            List<Seguro> seguros = seguroService.obtenerTodosSeguros();

            if (seguros == null || seguros.isEmpty()) {
                System.out.println("No se encontraron seguros en la base de datos. Añadiendo 'vacio' a la lista.");
                Seguro seguroVacio = new Seguro();
                seguroVacio.setNombre("vacio");
                seguros = Collections.singletonList(seguroVacio);
            }

            System.out.println("Seguros obtenidos: " + seguros.size());
            return new ResponseEntity<>(seguros, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error al obtener todos los seguros: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para obtener todos los seguros de un tipo específico.
     * 
     * @param tipoSeguro Tipo de seguro.
     * @return Lista de seguros del tipo especificado o un seguro vacío si no hay
     *         resultados.
     */
    @GetMapping("/seguro/obtenerPorTipo")
    public ResponseEntity<List<Seguro>> obtenerSegurosPorTipo(@RequestParam("tipoSeguro") String tipoSeguro) {
        System.out.println("Petición recibida para obtener seguros por tipo con tipo: " + tipoSeguro);
        try {
            List<Seguro> seguros = seguroService.obtenerSegurosPorTipo(tipoSeguro);
            if (seguros == null || seguros.isEmpty()) {
                System.out.println(
                        "No se encontraron seguros para el tipo: " + tipoSeguro + ". Añadiendo 'vacio' a la lista.");
                Seguro seguroVacio = new Seguro();
                seguroVacio.setNombre("vacio");
                seguros = Collections.singletonList(seguroVacio);
            }

            System.out.println("Seguros encontrados: " + seguros.size());
            return new ResponseEntity<>(seguros, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error al obtener seguros por tipo: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para asociar un seguro a un cliente.
     * 
     * @param seguroId  ID del seguro.
     * @param clienteId ID del cliente.
     * @return Respuesta HTTP indicando el resultado de la operación.
     */
    @PostMapping("/seguro/seleccionar")
    public ResponseEntity<String> seleccionarSeguro(
            @RequestParam("seguroId") Long seguroId,
            @RequestParam("clienteId") Long clienteId) {
        try {
            if (seguroId == null || seguroId <= 0 || clienteId == null || clienteId <= 0) {
                return ResponseEntity.badRequest()
                        .body("Los IDs del seguro y del cliente son obligatorios y deben ser mayores a 0.");
            }

            boolean seleccionado = seguroService.seleccionarSeguro(seguroId, clienteId);
            if (seleccionado) {
                return ResponseEntity.ok("Seguro seleccionado correctamente");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se pudo seleccionar el seguro. Verifique los IDs proporcionados.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al seleccionar el seguro: " + e.getMessage());
        }
    }

    /**
     * Endpoint para obtener todos los seguros asociados a un cliente.
     * 
     * @param clienteId ID del cliente.
     * @return Lista de seguros asociados al cliente o NO_CONTENT si no hay
     *         resultados.
     */
    @GetMapping("/porCliente")
    public ResponseEntity<List<Seguro>> getSegurosPorCliente(
            @RequestParam("clienteId") Long clienteId) {
        List<Seguro> lista = seguroService.obtenerPorCliente(clienteId);
        if (lista == null || lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    /**
     * Endpoint para obtener la cantidad de clientes por seguro.
     * 
     * @return Lista de objetos ClientesPorSeguro con el nombre del seguro y la
     *         cantidad de clientes.
     */
    @GetMapping("/cantidadClientes")
    public ResponseEntity<List<ClientesPorSeguro>> cantidadClientesPorSeguro() {
        List<ClientesPorSeguro> stats = seguroService.contarClientesPorSeguro();
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/guardarCoche")
    public ResponseEntity<?> guardarSeguroCoche(
            @RequestParam("seguroId") Long seguroId,
            @RequestParam("matricula") String matricula,
            @RequestParam("modelo") String modelo,
            @RequestParam("marca") String marca) {
        try {
            // Validaciones básicas
            if (seguroId == null || seguroId <= 0 || matricula == null || matricula.isBlank()
                    || modelo == null || modelo.isBlank() || marca == null || marca.isBlank()) {
                return ResponseEntity.badRequest().body("Todos los campos son obligatorios.");
            }

            Seguro seguro = seguroService.obtenerSeguroPorId(seguroId);
            if (seguro == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguro no encontrado.");
            }

            SeguroCoche seguroCoche = seguroService.guardarSeguroCoche(seguro, matricula, modelo, marca);
            return ResponseEntity.ok(seguroCoche);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el seguro de coche: " + e.getMessage());
        }
    }

    @PostMapping("/guardarVida")
public ResponseEntity<?> guardarSeguroVida(
        @RequestParam("clienteId") Long clienteId,
        @RequestParam("seguroId") Long seguroId,
        @RequestParam("edad") Integer edadAsegurado,
        @RequestParam("beneficiarios") String beneficiarios) {
    try {
        if (clienteId == null || clienteId <= 0 ||
            seguroId == null || seguroId <= 0 ||
            edadAsegurado == null || edadAsegurado <= 0 ||
            beneficiarios == null || beneficiarios.isBlank()) {
            return ResponseEntity.badRequest().body("Todos los campos son obligatorios.");
        }

        System.out.println("Petición recibida para guardar seguro de vida con ID: " + seguroId);

        Seguro seguro = seguroService.obtenerSeguroPorId(seguroId);
        if (seguro == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguro no encontrado.");
        }

        Cliente cliente = seguroService.clienterepo.findById(clienteId).orElse(null);
        if (cliente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado.");
        }

        SeguroVida seguroVida = seguroService.guardarSeguroVida(seguro, cliente, edadAsegurado, beneficiarios);
        return ResponseEntity.ok(seguroVida);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al guardar el seguro de vida: " + e.getMessage());
    }
}

    @PostMapping("/guardarCasa")
    public ResponseEntity<?> guardarSeguroCasa(
            @RequestParam("seguroId") Long seguroId,
            @RequestParam("direccion") String direccion,
            @RequestParam("valorInmueble") Double valorInmueble,
            @RequestParam("tipoVivienda") String tipoVivienda) {
        try {
            if (seguroId == null || seguroId <= 0 ||
                direccion == null || direccion.isBlank() ||
                valorInmueble == null || valorInmueble <= 0 ||
                tipoVivienda == null || tipoVivienda.isBlank()) {
                return ResponseEntity.badRequest().body("Todos los campos son obligatorios.");
            }

            Seguro seguro = seguroService.obtenerSeguroPorId(seguroId);
            if (seguro == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seguro no encontrado.");
            }

            SeguroCasa seguroCasa = seguroService.guardarSeguroCasa(seguro, direccion, valorInmueble, tipoVivienda);
            return ResponseEntity.ok(seguroCasa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar el seguro de casa: " + e.getMessage());
        }
    }
}