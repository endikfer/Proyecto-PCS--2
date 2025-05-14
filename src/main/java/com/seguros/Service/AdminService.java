package com.seguros.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seguros.repository.DudaRepository;

@Service
public class AdminService {

    private final DudaRepository dudaRepository;

    public AdminService(DudaRepository dudaRepository) {
        this.dudaRepository = dudaRepository;
    }

    public List<String> getAllAsuntoDudas() {
        List<String> asuntos = dudaRepository.findAllAsuntos();

        if (asuntos == null || asuntos.isEmpty()) {
            System.out.println("No se encontraron seguros en la base de datos.");
            return new ArrayList<>();
        }

        return asuntos;
    }

    public String getMensajeByAsuntoDudas(String asunto ) {
        String asuntoProcesado = SeguroService.restaurarEspacios(asunto);

        System.out.println("Asunto procesado: " + asuntoProcesado);

        String mensaje = dudaRepository.findMensajeByAsunto(asuntoProcesado);

        if (mensaje == null || mensaje.trim().isEmpty()) {
            System.out.println("No se encontró un mensaje para el asunto proporcionado.");
            return "No hay mensaje para este asunto.";
        }

        return mensaje;
    }
}
