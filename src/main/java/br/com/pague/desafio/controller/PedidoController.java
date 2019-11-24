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

import br.com.pague.desafio.domain.Pedido;
import br.com.pague.desafio.repository.PedidoRepository;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@GetMapping
	public List<Pedido> listar(@RequestParam(required = false) Long clienteId) {
		List<Pedido> pedidos = null;
		if (clienteId == null) {
			pedidos = pedidoRepository.findAll();
		} else {
			pedidos = pedidoRepository.findAllByClienteId(clienteId);
		}
		return pedidos;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<Pedido> cadastrar(@RequestBody @Valid Pedido pedido, UriComponentsBuilder uriBuilder) {
		pedidoRepository.save(pedido);
		URI uri = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedido.getId()).toUri();
		return ResponseEntity.created(uri).body(pedido);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Pedido> atualizar(@PathVariable Long id, @RequestBody @Valid Pedido form) {
		Optional<Pedido> optional = pedidoRepository.findById(id);
		if (optional.isPresent()) {
			Pedido pedido = optional.get();
			
			return ResponseEntity.ok(pedido);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Pedido> obter(@PathVariable Long id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		if (pedido.isPresent()) {
			return ResponseEntity.ok(pedido.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<Pedido> remover(@PathVariable Long id) {
		Optional<Pedido> optional = pedidoRepository.findById(id);
		if (optional.isPresent()) {
			pedidoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
