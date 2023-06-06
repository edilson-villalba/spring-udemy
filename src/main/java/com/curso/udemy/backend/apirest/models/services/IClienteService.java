package com.curso.udemy.backend.apirest.models.services;

import java.util.List;

import com.curso.udemy.backend.apirest.models.entity.Cliente;

public interface IClienteService {

	public List<Cliente> findall();
	public Cliente findById(Long id);
	public Cliente save(Cliente client);
	public void delete(Long id);
}
