package br.com.pague.desafio.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import br.com.pague.desafio.domain.Cliente;
import br.com.pague.desafio.repository.ClienteRepository;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@GetMapping
	public List<Cliente> listar(@RequestParam(required = false) String nome) {
		List<Cliente> clientes = null;
		if (nome == null) {
			clientes = clienteRepository.findAll();
		} else {
			clientes = clienteRepository.findByNome(nome);
		}
		return clientes;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<Cliente> cadastrar(@RequestBody @Valid Cliente cliente, UriComponentsBuilder uriBuilder) {
		clienteRepository.save(cliente);
		URI uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).body(cliente);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody @Valid Cliente form) {
		Optional<Cliente> optional = clienteRepository.findById(id);
		if (optional.isPresent()) {
			Cliente cliente = optional.get();
			
			cliente.setNome(form.getNome());
			cliente.setCpf(form.getCpf());
			cliente.setDataNascimento(form.getDataNascimento());
			
			return ResponseEntity.ok(cliente);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> obter(@PathVariable Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if (cliente.isPresent()) {
			return ResponseEntity.ok(cliente.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Cliente> remover(@PathVariable Long id) {
		Optional<Cliente> optional = clienteRepository.findById(id);
		if (optional.isPresent()) {
			clienteRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
