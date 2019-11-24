package br.com.pague.desafio.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import br.com.pague.desafio.controller.dto.PedidoDTO;
import br.com.pague.desafio.controller.mapper.PedidoMapper;
import br.com.pague.desafio.controller.util.HeaderUtil;
import br.com.pague.desafio.domain.Pedido;
import br.com.pague.desafio.repository.ClienteRepository;
import br.com.pague.desafio.repository.PedidoRepository;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/pedidos")
@Api(tags = "Pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PedidoMapper pedidoMapper;
	
	private HttpHeaders httpPedidoNaoEncontrado = HeaderUtil.createFailureAlert("PEDIDO", "PEDIDO_NAO_ENCONTRADO", "Pedido não encontrado");
	
	private HttpHeaders httpClienteNaoEncontrado = HeaderUtil.createFailureAlert("CLIENTE", "CLIENTE_NAO_ENCONTRADO", "Cliente não encontrado");
	
	@GetMapping
	public List<PedidoDTO> listar(@RequestParam(required = false) Long clienteId) {
		List<Pedido> pedidos = null;
		if (clienteId == null) {
			pedidos = pedidoRepository.findAll();
		} else {
			pedidos = pedidoRepository.findAllByClienteId(clienteId);
		}
		
		return pedidoMapper.toDto(pedidos);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<PedidoDTO> cadastrar(@RequestBody @Valid PedidoDTO pedidoDto, UriComponentsBuilder uriBuilder) {
		Pedido pedido = pedidoMapper.toEntity(pedidoDto);
		pedido = pedidoRepository.save(pedido);
		URI uri = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedido.getId()).toUri();
		return ResponseEntity.created(uri).body(pedidoMapper.toDto(pedido));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<PedidoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PedidoDTO pedidoDto) {
		Optional<Pedido> optional = pedidoRepository.findById(id);
		if (optional.isPresent()) {
			Pedido pedido = optional.get();
			return ResponseEntity.ok(pedidoMapper.toDto(pedido));
		}
		
		return ResponseEntity.notFound()
				.headers(httpPedidoNaoEncontrado)
				.build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PedidoDTO> obter(@PathVariable Long id) {
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		if (pedido.isPresent()) {
			return ResponseEntity.ok(pedidoMapper.toDto(pedido.get()));
		}
		
		return ResponseEntity.notFound()
				.headers(httpPedidoNaoEncontrado)
				.build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<PedidoDTO> remover(@PathVariable Long id) {
		Optional<Pedido> optional = pedidoRepository.findById(id);
		if (optional.isPresent()) {
			pedidoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound()
				.headers(httpPedidoNaoEncontrado)
				.build();
	}
}
