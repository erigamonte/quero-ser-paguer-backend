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
import br.com.pague.desafio.service.ItemPedidoService;
import br.com.pague.desafio.service.dto.ItemPedidoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/item-pedidos")
@Api(tags = "Itens de Pedido")
public class ItemPedidoController {
	
	@Autowired
	private ItemPedidoService itemPedidoService;
	
	@GetMapping
	@ApiOperation(value = "Buscar itens de um pedido", notes = "Buscar itens de um pedido")
	public List<ItemPedidoDTO> listar(@RequestParam(required = true) Long pedidoId) {
		return itemPedidoService.obtemTodos(pedidoId);
	}
	
	@PostMapping
	@Transactional
	@ApiOperation(value = "Cadastrar item de pedido", notes = "Cadastrar item de pedido")
	public ResponseEntity<ItemPedidoDTO> cadastrar(@RequestBody @Valid ItemPedidoDTO itemPedidoDto, UriComponentsBuilder uriBuilder) {
		ItemPedidoDTO itemPedido;
		try {
			itemPedido = itemPedidoService.salva(itemPedidoDto);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("ITEM_PEDIDO", e.getCode(), e.getMessage()))
					.body(itemPedidoDto);
		}
		URI uri = uriBuilder.path("/itemPedidos/{id}").buildAndExpand(itemPedido.getId()).toUri();
		return ResponseEntity.created(uri).body(itemPedido);
	}
	
	@PutMapping("/{id}")
	@Transactional
	@ApiOperation(value = "Atualizar item de pedido", notes = "Atualizar item de pedido")
	public ResponseEntity<ItemPedidoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ItemPedidoDTO itemPedidoDto) {
		ItemPedidoDTO itemPedido;
		try {
			itemPedidoDto.setId(id);
			itemPedido = itemPedidoService.salva(itemPedidoDto);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("ITEM_PEDIDO", e.getCode(), e.getMessage()))
					.body(itemPedidoDto);
		}
			
		return ResponseEntity.ok(itemPedido);
	}
	
	@GetMapping("/{id}")
	@ApiOperation(value = "Detalhar item de pedido", notes = "Detalhar item de pedido")
	public ResponseEntity<ItemPedidoDTO> obter(@PathVariable Long id) {
		ItemPedidoDTO itemPedido = itemPedidoService.obtemPeloId(id);
		if (itemPedido != null) {
			return ResponseEntity.ok(itemPedido);
		}
		
		return ResponseEntity.notFound()
				.headers(HeaderUtil.createFailureAlert("ITEM_PEDIDO", "ITEM_PEDIDO_NAO_ENCONTRADO", "Item de Pedido não encontrado"))
				.build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@ApiOperation(value = "Remover item de pedido", notes = "Remover item de pedido")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			itemPedidoService.remove(id);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("ITEM_PEDIDO", e.getCode(), e.getMessage()))
					.body(null);
		}
		
		return ResponseEntity.noContent().build();
	}
}
