package com.seguros.Service;

import org.springframework.stereotype.Service;

import com.seguros.repository.AdministradorRepository;

@Service
public class UsuarioService {

    private final AdministradorRepository administradorRepository;

    public UsuarioService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    public boolean esAdmin(String username) {
        return administradorRepository.existsByUsername(username);
    }

}
