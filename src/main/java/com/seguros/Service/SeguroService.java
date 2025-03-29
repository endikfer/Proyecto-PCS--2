package com.seguros.Service;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.seguros.model.Seguro;
import com.seguros.model.TipoSeguro;
import com.seguros.repository.SeguroRepository;

@Service
public class SeguroService {

    public SeguroRepository segurorepo;

    public void crearReto(String nombre, String descripcion, String tipoSeguro, Double precio) {
        TipoSeguro tipoSeguro1;
        try {
            tipoSeguro1 = TipoSeguro.valueOf(tipoSeguro.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "El tipo de seguro '" + tipoSeguro + "' no es válido. Los valores permitidos son: "
                            + Arrays.toString(TipoSeguro.values()));
        }

        Seguro seguro = new Seguro(nombre, descripcion, tipoSeguro1, precio);
        segurorepo.save(seguro);
    }

}
