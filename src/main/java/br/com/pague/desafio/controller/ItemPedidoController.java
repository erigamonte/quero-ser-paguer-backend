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

import br.com.pague.desafio.controller.form.AtualizacaoItemPedidoForm;
import br.com.pague.desafio.domain.ItemPedido;
import br.com.pague.desafio.domain.Pedido;
import br.com.pague.desafio.domain.Produto;
import br.com.pague.desafio.repository.ItemPedidoRepository;
import br.com.pague.desafio.repository.PedidoRepository;
import br.com.pague.desafio.repository.ProdutoRepository;

@RestController
@RequestMapping("/item-pedidos")
public class ItemPedidoController {
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public List<ItemPedido> listar(@RequestParam(required = false) Long pedidoId) {
		List<ItemPedido> itemPedidos = itemPedidoRepository.findAllByPedidoId(pedidoId);
		return itemPedidos;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<ItemPedido> cadastrar(@RequestBody @Valid ItemPedido itemPedido, UriComponentsBuilder uriBuilder) {
		Optional<Pedido> optionalPedido = pedidoRepository.findById(itemPedido.getPedido().getId());
		Optional<Produto> optionalProduto = produtoRepository.findById(itemPedido.getProduto().getId());
		
		if (optionalPedido.isPresent() && optionalProduto.isPresent()) {
			itemPedido.setPreco(optionalProduto.get().getPrecoSugerido());
			itemPedidoRepository.save(itemPedido);
			URI uri = uriBuilder.path("/item-pedidos/{id}").buildAndExpand(itemPedido.getId()).toUri();
			return ResponseEntity.created(uri).body(itemPedido);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ItemPedido> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoItemPedidoForm form) {
		Optional<ItemPedido> optional = itemPedidoRepository.findById(id);
		if (optional.isPresent()) {
			ItemPedido itemPedido = optional.get();
			itemPedido.setQuantidade(form.getQuantidade());
			return ResponseEntity.ok(itemPedido);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ItemPedido> obter(@PathVariable Long id) {
		Optional<ItemPedido> itemPedido = itemPedidoRepository.findById(id);
		if (itemPedido.isPresent()) {
			return ResponseEntity.ok(itemPedido.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<ItemPedido> remover(@PathVariable Long id) {
		Optional<ItemPedido> optional = itemPedidoRepository.findById(id);
		if (optional.isPresent()) {
			itemPedidoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
