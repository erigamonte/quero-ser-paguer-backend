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
import br.com.pague.desafio.service.PedidoService;
import br.com.pague.desafio.service.dto.PedidoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/pedidos")
@Api(tags = "Pedidos")
public class PedidoController {
	
	@Autowired
	private PedidoService pedidoService;

	@GetMapping
	@ApiOperation(value = "Buscar Pedidos", notes = "Buscar Pedidos")
	public List<PedidoDTO> listar(@RequestParam(required = false) Long idCliente) {
		return pedidoService.obtemTodos(idCliente);
	}
	
	@PostMapping
	@Transactional
	@ApiOperation(value = "Cadastrar Pedido", notes = "Cadastrar Pedido")
	public ResponseEntity<PedidoDTO> cadastrar(@RequestBody @Valid PedidoDTO pedidoDto, UriComponentsBuilder uriBuilder) {
		PedidoDTO pedido;
		try {
			pedido = pedidoService.salva(pedidoDto);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("PEDIDO", e.getCode(), e.getMessage()))
					.body(pedidoDto);
		}
		URI uri = uriBuilder.path("/pedidos/{id}").buildAndExpand(pedido.getId()).toUri();
		return ResponseEntity.created(uri).body(pedido);
	}
	
	@PutMapping("/{id}")
	@Transactional
	@ApiOperation(value = "Atualizar Pedido", notes = "Atualizar Pedido")
	public ResponseEntity<PedidoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PedidoDTO pedidoDto) {
		PedidoDTO pedido;
		try {
			pedidoDto.setId(id);
			pedido = pedidoService.salva(pedidoDto);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("PEDIDO", e.getCode(), e.getMessage()))
					.body(pedidoDto);
		}
			
		return ResponseEntity.ok(pedido);
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Detalhar Pedido", notes = "Detalhar Pedido")
	public ResponseEntity<PedidoDTO> obter(@PathVariable Long id) {
		PedidoDTO pedido = pedidoService.obtemPeloId(id);
		if (pedido != null) {
			return ResponseEntity.ok(pedido);
		}
		
		return ResponseEntity.notFound()
				.headers(HeaderUtil.createFailureAlert("PEDIDO", "PEDIDO_NAO_ENCONTRADO", "Pedido não encontrado"))
				.build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@ApiOperation(value = "Remover Pedido", notes = "Remover Pedido")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			pedidoService.remove(id);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("PEDIDO", e.getCode(), e.getMessage()))
					.body(null);
		}
		
		return ResponseEntity.noContent().build();
	}
}
