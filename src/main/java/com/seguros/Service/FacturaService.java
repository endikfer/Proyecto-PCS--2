package com.seguros.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.seguros.model.Factura;
import com.seguros.repository.FacturaRepository;



@Service
public class FacturaService {
    @Autowired
    private FacturaRepository facturaRepository;

    public List<Factura> getFacturasByClienteId(Long clienteId) {
        return facturaRepository.findByIdCliente(clienteId);
    }
    
}
