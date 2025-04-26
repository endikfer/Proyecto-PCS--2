package com.seguros.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seguros.model.Cliente;
import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;
import com.seguros.repository.ClienteRepository;
import com.seguros.repository.SeguroRepository;

@Service
public class SeguroService {

    @Autowired
    public SeguroRepository segurorepo;
    

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

    public Seguro obtenerSeguroPorNombre(String nombre) {
        return segurorepo.findByNombre(restaurarEspacios(nombre));
    }

    public static String restaurarEspacios(String texto) {
        if (texto == null)
            return null;
        // System.out.println("Texto original: " + texto);
        // System.out.println("Texto con espacios: " + texto.replace(';', ' '));
        return texto.replace(';', ' ');
    }

    public boolean eliminarSeguro(Long id) {
        return segurorepo.findById(id).map(seguro -> {
            segurorepo.delete(seguro);
            return true;
        }).orElse(false);
    }

    public List<Seguro> obtenerTodosSeguros() {
        List<Seguro> seguros = segurorepo.findAll(); // Obtiene todos los seguros de la base de datos

        if (seguros == null || seguros.isEmpty()) {
            System.out.println("No se encontraron seguros en la base de datos.");
            return new ArrayList<>(); // Devuelve una lista vacía si no hay seguros
        }

        return seguros; // Devuelve la lista de seguros
    }

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

    @Autowired
    private ClienteRepository clienteRepository;

    public boolean seleccionarSeguro(Long seguroId, Long clienteId) {
        System.out.println("Petición recibida para seleccionar seguro. SeguroId: " + seguroId + ", ClienteId: " + clienteId);

        var seguroOptional = segurorepo.findById(seguroId);
        var clienteOptional = clienteRepository.findById(clienteId);

        if (seguroOptional.isPresent() && clienteOptional.isPresent()) {
            Seguro seguro = seguroOptional.get();
            Cliente cliente = clienteOptional.get();

            cliente.setSeguroSeleccionado(seguro);

            clienteRepository.save(cliente);

            System.out.println("Seguro seleccionado y asociado al cliente correctamente");
            return true;
        } else {
            System.out.println("No se encontró el seguro o el cliente");
            return false;
        }
    }

    public List<Seguro> obtenerPorCliente(Long clienteId) {
        // delegamos directamente al repositorio JPA
        return segurorepo.findByClienteId(clienteId);
    }
    
    public List<ClientesPorSeguro> contarClientesPorSeguro() {
        // Primero obtenemos todos los seguros
        List<Seguro> seguros = segurorepo.findAll();
        // Para cada uno contamos cuántos clientes lo tienen asignado
        List<ClientesPorSeguro> resultado = new ArrayList<>();
        for (Seguro s : seguros) {
            long count = clienteRepository.countBySeguroSeleccionado_Id(s.getId());
            resultado.add(new ClientesPorSeguro(s.getNombre(), count));
        }
        return resultado;
    }

}