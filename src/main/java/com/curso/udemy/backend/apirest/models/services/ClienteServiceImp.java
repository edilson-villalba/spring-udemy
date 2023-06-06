package com.curso.udemy.backend.apirest.models.services;

import java.util.List;

import com.curso.udemy.backend.apirest.models.dao.IClienteDao;
import com.curso.udemy.backend.apirest.models.entity.Cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImp implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findall() {
		return	(List<Cliente>) clienteDao.findAll();
		
	}
	@Override
	@Transactional(readOnly = true)
	public Cliente findById(Long id) {
		return clienteDao.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public Cliente save(Cliente client) {
		return clienteDao.save(client);
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

}
