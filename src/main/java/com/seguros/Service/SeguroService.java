package com.seguros.Service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;
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

        Seguro seguro = new Seguro(nombre, descripcion, tipoSeguro1, precio);
        System.out.println("Seguro creado: " + seguro);

        if (segurorepo == null) {
            System.out.println("El repositorio segurorepo es null");
            throw new IllegalStateException("El repositorio segurorepo no está inicializado");
        }

        segurorepo.save(seguro);
        System.out.println("Seguro guardado en la base de datos");
    }

    public boolean editarSeguro(Long id, String nombre, String descripcion, String tipoSeguro, Double precio) {
        System.out.println("Petición recibida para editar seguro desde service\n");

        TipoSeguro tipoSeguro1;
        System.out.println("Tipo de seguro recibido: " + tipoSeguro);
        try {
            tipoSeguro1 = TipoSeguro.valueOf(tipoSeguro.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "El tipo de seguro '" + tipoSeguro + "' no es válido. Los valores permitidos son: "
                            + Arrays.toString(TipoSeguro.values()));
        }

        System.out.println("Tipo de seguro convertido: " + tipoSeguro1);

        return segurorepo.findById(id).map(seguro -> {
            seguro.setNombre(nombre.trim());
            seguro.setDescripcion(descripcion.trim());
            seguro.setTipoSeguro(tipoSeguro1);
            seguro.setPrecio(precio);
            segurorepo.save(seguro);
            System.out.println("Seguro actualizado en la base de datos");
            return true;
        }).orElse(false);
    }

    public Seguro obtenerSeguroPorNombre(String nombre) {
        return segurorepo.findByNombre(restaurarEspacios(nombre));
    }

    public static String restaurarEspacios(String texto) {
        if (texto == null) return null;
        System.out.println("Texto original: " + texto);
        System.out.println("Texto con espacios: " + texto.replace(';', ' '));
        return texto.replace(';', ' ');
    }

}