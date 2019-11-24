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

import br.com.pague.desafio.controller.dto.ItemPedidoDTO;
import br.com.pague.desafio.controller.mapper.ItemPedidoMapper;
import br.com.pague.desafio.controller.util.HeaderUtil;
import br.com.pague.desafio.domain.ItemPedido;
import br.com.pague.desafio.domain.Produto;
import br.com.pague.desafio.repository.ItemPedidoRepository;
import br.com.pague.desafio.repository.ProdutoRepository;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/item-pedidos")
@Api(tags = "Itens de Pedido")
public class ItemPedidoController {
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoMapper itemPedidoMapper;
	
	private HttpHeaders httpItemNaopEncontrado = HeaderUtil.createFailureAlert("ITEM_PEDIDO", "ITEM_PEDIDO_NAO_ENCONTRADO", "Item Pedido não encontrado");
	
	@GetMapping
	public List<ItemPedidoDTO> listar(@RequestParam(required = true) Long pedidoId) {
		List<ItemPedido> itemPedidos = itemPedidoRepository.findAllByPedidoId(pedidoId);
		return itemPedidoMapper.toDto(itemPedidos);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<ItemPedidoDTO> cadastrar(@RequestBody @Valid ItemPedidoDTO itemPedidoDto, UriComponentsBuilder uriBuilder) {
		Produto produto = produtoRepository.getOne(itemPedidoDto.getProdutoId());
		itemPedidoDto.setPreco(produto.getPrecoSugerido());
		itemPedidoRepository.save(itemPedidoMapper.toEntity(itemPedidoDto));
		URI uri = uriBuilder.path("/item-pedidos/{id}").buildAndExpand(itemPedidoDto.getId()).toUri();
		return ResponseEntity.created(uri).body(itemPedidoDto);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ItemPedidoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ItemPedidoDTO form) {
		Optional<ItemPedido> optional = itemPedidoRepository.findById(id);
		if (optional.isPresent()) {
			ItemPedido itemPedido = optional.get();
			itemPedido.setQuantidade(form.getQuantidade());
			return ResponseEntity.ok(itemPedidoMapper.toDto(itemPedido));
		}
		
		return ResponseEntity.notFound()
				.headers(httpItemNaopEncontrado)
				.build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ItemPedidoDTO> obter(@PathVariable Long id) {
		Optional<ItemPedido> itemPedido = itemPedidoRepository.findById(id);
		if (itemPedido.isPresent()) {
			return ResponseEntity.ok(itemPedidoMapper.toDto(itemPedido.get()));
		}
		
		return ResponseEntity.notFound()
				.headers(httpItemNaopEncontrado)
				.build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<ItemPedidoDTO> remover(@PathVariable Long id) {
		Optional<ItemPedido> optional = itemPedidoRepository.findById(id);
		if (optional.isPresent()) {
			itemPedidoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound()
				.headers(httpItemNaopEncontrado)
				.build();
	}
}
