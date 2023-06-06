package com.curso.udemy.backend.apirest.models.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import com.curso.udemy.backend.apirest.models.entity.Cliente;
import com.curso.udemy.backend.apirest.models.services.IClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clientes")
	public List<Cliente> listarCliente() {
		return clienteService.findall();
	}

	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> buscarCliente(@PathVariable Long id) {

		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();

		try {
			cliente = clienteService.findById(id);
			System.out.println("Cliente " + cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}

	@PostMapping("/clientes")
	public ResponseEntity<?> crearCliente(@Valid @RequestBody Cliente cliente, BindingResult result) {
		Cliente dao = null;
		Map<String, Object> response = new HashMap<>();
		// Manejador de errores
		if (result.hasErrors()) {
			/*
			 * List<String> errors = ArrayList<>();Menor a JDK8 for(FieldError err:
			 * result.getFieldErrors()) { errors.add("El campo '"+ err.getField()+"'"+
			 * err.getDefaultMessage()); }
			 */
			// superior a JDK 8
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "'" + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			cliente.setCreateAt(new Date());
			dao = this.clienteService.save(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la inserción en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensajeExito", "El cliente ha sido creado con exito");
		response.put("cliente", dao);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> modificarCliente(@Valid @RequestBody Cliente cliente, @PathVariable Long id,
			BindingResult result) {
		Cliente cli_actual = clienteService.findById((id));

		Cliente cliente_modif = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream()
					.map(err -> "El campo '" + err.getField() + "'" + err.getDefaultMessage())
					.collect(Collectors.toList());

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		if (cli_actual == null) {
			response.put("mensaje",
					"Error no se puede editar cliente ".concat(id.toString().concat(", No existe el id.")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			cli_actual.setNombre(cliente.getNombre());
			cli_actual.setApellido(cliente.getApellido());
			cli_actual.setEmail(cliente.getEmail());
			cliente_modif = clienteService.save(cli_actual);

		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la actualización en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensajeExito", "El cliente ha sido actualizado con exito...!!!");
		response.put("cliente", cliente_modif);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@DeleteMapping("/clientes/{id}")
	private ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {

			clienteService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la eliminación en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		response.put("mensajeExito", "El cliente ha sido eliminado con exito...!!!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
