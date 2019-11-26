package br.com.pague.desafio.controller;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.pague.desafio.controller.error.DesafioException;
import br.com.pague.desafio.controller.util.HeaderUtil;
import br.com.pague.desafio.service.ClienteService;
import br.com.pague.desafio.service.dto.ClienteDTO;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/clientes")
@Api(tags = "Clientes")
public class ClienteController {
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping
	public List<ClienteDTO> listar(@RequestParam(required = false) String nome) {
		return clienteService.obtemTodos(nome);
	}
	
	@PostMapping
	public ResponseEntity<ClienteDTO> cadastrar(@RequestBody @Valid ClienteDTO clienteDto, UriComponentsBuilder uriBuilder) {
		ClienteDTO cliente;
		try {
			cliente = clienteService.salva(clienteDto);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("CLIENTE", e.getCode(), e.getMessage()))
					.body(clienteDto);
		}
		URI uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).body(cliente);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ClienteDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteDTO clienteDto) {
		ClienteDTO cliente;
		try {
			clienteDto.setId(id);
			cliente = clienteService.salva(clienteDto);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("CLIENTE", e.getCode(), e.getMessage()))
					.body(clienteDto);
		}
			
		return ResponseEntity.ok(cliente);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> obter(@PathVariable Long id) {
		ClienteDTO cliente = clienteService.obtemPeloId(id);
		if (cliente != null) {
			return ResponseEntity.ok(cliente);
		}
		
		return ResponseEntity.notFound()
				.headers(HeaderUtil.createFailureAlert("CLIENTE", "CLIENTE_NAO_ENCONTRADO", "Cliente não encontrado"))
				.build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			clienteService.remove(id);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("CLIENTE", e.getCode(), e.getMessage()))
					.body(null);
		}
		
		return ResponseEntity.noContent().build();
	}
}
