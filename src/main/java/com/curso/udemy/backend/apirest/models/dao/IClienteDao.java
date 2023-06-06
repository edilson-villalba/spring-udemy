package com.curso.udemy.backend.apirest.models.dao;

import com.curso.udemy.backend.apirest.models.entity.Cliente;

import org.springframework.data.repository.CrudRepository;

public interface IClienteDao extends CrudRepository<Cliente, Long>{

}
