package com.seguros.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.seguros.model.Duda;
import com.seguros.repository.DudaRepository;

/**
 * @class AdminService
 * @brief Servicio para la gestión de dudas administrativas.
 *
 *        Proporciona métodos para consultar asuntos y mensajes de dudas
 *        almacenadas en la base de datos.
 */
@Service
public class AdminService {

    /** Repositorio para acceder a las dudas. */
    private final DudaRepository dudaRepository;

    /**
     * Constructor que inyecta el repositorio de dudas.
     * 
     * @param dudaRepository Repositorio de dudas.
     */
    public AdminService(DudaRepository dudaRepository) {
        this.dudaRepository = dudaRepository;
    }

    /**
     * Obtiene todos los asuntos de las dudas almacenadas.
     * 
     * @return Lista de asuntos de dudas.
     */
    public List<String> getAllAsuntoDudas() {
        List<String> asuntos = dudaRepository.findAllAsuntos();

        if (asuntos == null || asuntos.isEmpty()) {
            System.out.println("No se encontraron seguros en la base de datos.");
            return new ArrayList<>();
        }

        return asuntos;
    }

    /**
     * Obtiene el mensaje asociado a un asunto de duda.
     * 
     * @param asunto Asunto de la duda.
     * @return Mensaje correspondiente al asunto, o mensaje por defecto si no
     *         existe.
     */
    public String getMensajeByAsuntoDudas(String asunto) {
        String asuntoProcesado = SeguroService.restaurarEspacios(asunto);

        System.out.println("Asunto procesado: " + asuntoProcesado);

        String mensaje = dudaRepository.findMensajeByAsunto(asuntoProcesado);

        if (mensaje == null || mensaje.trim().isEmpty()) {
            System.out.println("No se encontró un mensaje para el asunto proporcionado.");
            return "No hay mensaje para este asunto.";
        }

        return mensaje;
    }

    public void enviarDuda(String asunto, String mensaje) {
        Duda duda = new Duda(asunto, mensaje);
        dudaRepository.save(duda);
    }
}