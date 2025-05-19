package com.seguros.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seguros.model.Cliente;
import com.seguros.model.Seguro;
import com.seguros.model.SeguroCasa;
import com.seguros.model.SeguroCoche;
import com.seguros.model.SeguroVida;
import com.seguros.model.TipoSeguro;
import com.seguros.repository.ClienteRepository;
import com.seguros.repository.SeguroCasaRepository;
import com.seguros.repository.SeguroCocheRepository;
import com.seguros.repository.SeguroRepository;
import com.seguros.repository.SeguroVidaRepository;

/**
 * @class SeguroService
 * @brief Servicio para la gestión de seguros.
 *
 *        Proporciona métodos para crear, editar, eliminar, consultar y asociar
 *        seguros a clientes.
 */
@Service
public class SeguroService {

    /** Repositorio para acceder a los seguros. */
    @Autowired
    public final SeguroRepository segurorepo;
    public final ClienteRepository clienterepo;
    public final SeguroCocheRepository seguroCocheRepository;
    public final SeguroVidaRepository seguroVidaRepository;
    public final SeguroCasaRepository seguroCasaRepository;
    
    public SeguroService(SeguroRepository segurorepo, ClienteRepository clienterepo, SeguroCocheRepository seguroCocheRepository,SeguroCasaRepository seguroCasaRepository, SeguroVidaRepository seguroVidaRepository) {
        this.segurorepo = segurorepo;
        this.clienterepo = clienterepo;
        this.seguroCocheRepository = seguroCocheRepository;
        this.seguroCasaRepository = seguroCasaRepository;
        this.seguroVidaRepository = seguroVidaRepository;
    }

    /**
     * Crea un nuevo seguro y lo guarda en la base de datos.
     * 
     * @param nombre      Nombre del seguro.
     * @param descripcion Descripción del seguro.
     * @param tipoSeguro  Tipo de seguro (como String).
     * @param precio      Precio del seguro.
     * @throws IllegalArgumentException Si el tipo de seguro no es válido.
     * @throws IllegalStateException    Si el repositorio no está inicializado.
     */
    public void crearSeguro(String nombre, String descripcion, String tipoSeguro, Double precio) {
        System.out.println("Peticion recibida para crear seguro desde service\n");

        TipoSeguro tipoSeguro1;
        try {
            tipoSeguro1 = TipoSeguro.valueOf(tipoSeguro.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "El tipo de seguro '" + tipoSeguro + "' no es válido. Los valores permitidos son: "
                            + Arrays.toString(TipoSeguro.values()));
        }

        Seguro seguro = new Seguro(restaurarEspacios(nombre), restaurarEspacios(descripcion), tipoSeguro1, precio);
        System.out.println("Seguro creado: " + seguro);

        if (segurorepo == null) {
            System.out.println("El repositorio segurorepo es null");
            throw new IllegalStateException("El repositorio segurorepo no está inicializado");
        }

        segurorepo.save(seguro);
        System.out.println("Seguro guardado en la base de datos");
    }

    /**
     * Edita un seguro existente.
     * 
     * @param id          ID del seguro.
     * @param nombre      Nuevo nombre.
     * @param descripcion Nueva descripción.
     * @param tipoSeguro  Nuevo tipo de seguro.
     * @param precio      Nuevo precio.
     * @return true si se editó correctamente, false si no se encontró el seguro.
     * @throws IllegalArgumentException Si el tipo de seguro no es válido.
     */
    public boolean editarSeguro(Long id, String nombre, String descripcion, String tipoSeguro, Double precio) {

        TipoSeguro tipoSeguro1;
        try {
            tipoSeguro1 = TipoSeguro.valueOf(tipoSeguro.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "El tipo de seguro '" + tipoSeguro + "' no es válido. Los valores permitidos son: "
                            + Arrays.toString(TipoSeguro.values()));
        }

        return segurorepo.findById(id).map(seguro -> {
            seguro.setNombre(nombre.trim());
            seguro.setDescripcion(descripcion.trim());
            seguro.setTipoSeguro(tipoSeguro1);
            seguro.setPrecio(precio);
            segurorepo.save(seguro);
            return true;
        }).orElse(false);
    }

    /**
     * Obtiene un seguro por su nombre.
     * 
     * @param nombre Nombre del seguro.
     * @return Seguro correspondiente al nombre.
     */
    public Seguro obtenerSeguroPorNombre(String nombre) {
        return segurorepo.findByNombre(restaurarEspacios(nombre));
    }

    /**
     * Restaura los espacios en un texto reemplazando ';' por ' '.
     * 
     * @param texto Texto a procesar.
     * @return Texto con espacios restaurados.
     */
    public static String restaurarEspacios(String texto) {
        if (texto == null)
            return null;
        // System.out.println("Texto original: " + texto);
        // System.out.println("Texto con espacios: " + texto.replace(';', ' '));
        return texto.replace(';', ' ');
    }

    /**
     * Elimina un seguro por su ID.
     * 
     * @param id ID del seguro.
     * @return true si se eliminó correctamente, false si no se encontró el seguro.
     */
    public boolean eliminarSeguro(Long id) {
        return segurorepo.findById(id).map(seguro -> {
            segurorepo.delete(seguro);
            return true;
        }).orElse(false);
    }

    /**
     * Obtiene todos los seguros almacenados.
     * 
     * @return Lista de todos los seguros.
     */
    public List<Seguro> obtenerTodosSeguros() {
        List<Seguro> seguros = segurorepo.findAll(); // Obtiene todos los seguros de la base de datos

        if (seguros == null || seguros.isEmpty()) {
            System.out.println("No se encontraron seguros en la base de datos.");
            return new ArrayList<>(); // Devuelve una lista vacía si no hay seguros
        }

        return seguros; // Devuelve la lista de seguros
    }

    /**
     * Obtiene todos los seguros de un tipo específico.
     * 
     * @param tipoSeguro Tipo de seguro (como String).
     * @return Lista de seguros del tipo especificado.
     * @throws IllegalArgumentException Si el tipo de seguro no es válido.
     */
    public List<Seguro> obtenerSegurosPorTipo(String tipoSeguro) {
        TipoSeguro tipoSeguroEnum;
        try {
            tipoSeguroEnum = TipoSeguro.valueOf(tipoSeguro.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "El tipo de seguro '" + tipoSeguro + "' no es válido. Los valores permitidos son: "
                            + Arrays.toString(TipoSeguro.values()));
        }
        return segurorepo.findByTipoSeguro(tipoSeguroEnum);
    }

    /** Repositorio para acceder a los clientes. */
    //@Autowired

    /**
     * Asocia un seguro a un cliente.
     * 
     * @param seguroId  ID del seguro.
     * @param clienteId ID del cliente.
     * @return true si la asociación fue exitosa, false si no se encontró el seguro
     *         o el cliente.
     */
    public boolean seleccionarSeguro(Long seguroId, Long clienteId) {
        System.out.println(
                "Petición recibida para seleccionar seguro. SeguroId: " + seguroId + ", ClienteId: " + clienteId);

        var seguroOptional = segurorepo.findById(seguroId);
        var clienteOptional = clienterepo.findById(clienteId);

        if (seguroOptional.isPresent() && clienteOptional.isPresent()) {
            Seguro seguro = seguroOptional.get();
            Cliente cliente = clienteOptional.get();

            cliente.setSeguroSeleccionado(seguro);

            clienterepo.save(cliente);

            System.out.println("Seguro seleccionado y asociado al cliente correctamente");
            return true;
        } else {
            System.out.println("No se encontró el seguro o el cliente");
            return false;
        }
    }

    /**
     * Obtiene todos los seguros asociados a un cliente.
     * 
     * @param clienteId ID del cliente.
     * @return Lista de seguros asociados al cliente.
     */
    public List<Seguro> obtenerPorCliente(Long clienteId) {
        // delegamos directamente al repositorio JPA
        return segurorepo.findByClienteId(clienteId);
    }

    /**
     * Cuenta cuántos clientes tienen asignado cada seguro.
     * 
     * @return Lista de objetos ClientesPorSeguro con el nombre del seguro y la
     *         cantidad de clientes.
     */
    public List<ClientesPorSeguro> contarClientesPorSeguro() {
        // Primero obtenemos todos los seguros
        List<Seguro> seguros = segurorepo.findAll();
        // Para cada uno contamos cuántos clientes lo tienen asignado
        List<ClientesPorSeguro> resultado = new ArrayList<>();
        for (Seguro s : seguros) {
            long count = clienterepo.countBySeguroSeleccionado_Id(s.getId());
            resultado.add(new ClientesPorSeguro(s.getNombre(), count));
        }
        return resultado;
    }
    
    public List<Seguro> obtenerSegurosPorCliente(Long clienteId) {
        return segurorepo.findByClienteId(clienteId);
    }

    /**
     * Guarda un SeguroCoche en la base de datos.
     * 
     * @param seguro    El seguro asociado.
     * @param matricula Matrícula del coche.
     * @param modelo    Modelo del coche.
     * @param marca     Marca del coche.
     * @return El SeguroCoche guardado.
     */
    public SeguroCoche guardarSeguroCoche(Seguro seguro, String matricula, String modelo, String marca) {
        SeguroCoche seguroCoche = new SeguroCoche(seguro, matricula, modelo, marca);
        return seguroCocheRepository.save(seguroCoche);
    }

    public Seguro obtenerSeguroPorId(Long id) {
        return segurorepo.findById(id).orElse(null);
    }

    /**
     * Guarda un SeguroVida en la base de datos.
     *
     * @param seguro         El seguro asociado.
     * @param cliente        El cliente asociado.
     * @param edadAsegurado  Edad del asegurado.
     * @param beneficiarios  Beneficiarios.
     * @return El SeguroVida guardado.
     */
    public SeguroVida guardarSeguroVida(Seguro seguro, Cliente cliente, Integer edadAsegurado, String beneficiarios) {
        SeguroVida seguroVida = new SeguroVida(seguro, cliente, edadAsegurado, beneficiarios);
        return seguroVidaRepository.save(seguroVida);
    }

    public SeguroCasa guardarSeguroCasa(Seguro seguro, String direccion, Double valorInmueble, String tipoVivienda) {
        SeguroCasa seguroCasa = new SeguroCasa(seguro, direccion, valorInmueble, tipoVivienda);
        return seguroCasaRepository.save(seguroCasa);
    }
}