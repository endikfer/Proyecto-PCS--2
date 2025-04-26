package com.seguros.Service;

public class ClientesPorSeguro {
    private String nombreSeguro;
    private long cantidadClientes;
	public ClientesPorSeguro(String nombreSeguro, long cantidadClientes) {
		super();
		this.nombreSeguro = nombreSeguro;
		this.cantidadClientes = cantidadClientes;
	}
	public String getNombreSeguro() {
		return nombreSeguro;
	}
	public void setNombreSeguro(String nombreSeguro) {
		this.nombreSeguro = nombreSeguro;
	}
	public long getCantidadClientes() {
		return cantidadClientes;
	}
	public void setCantidadClientes(long cantidadClientes) {
		this.cantidadClientes = cantidadClientes;
	}

    // constructor, getters y setters
    
}
