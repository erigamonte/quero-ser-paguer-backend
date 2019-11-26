package br.com.pague.desafio.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

import br.com.pague.desafio.config.security.PerfilConstantes;
import br.com.pague.desafio.controller.util.HeaderUtil;
import br.com.pague.desafio.domain.Produto;
import br.com.pague.desafio.repository.ItemPedidoRepository;
import br.com.pague.desafio.repository.ProdutoRepository;
import br.com.pague.desafio.service.dto.ProdutoDTO;
import br.com.pague.desafio.service.mapper.ProdutoMapper;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/produtos")
@Api(tags = "Produtos")
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ProdutoMapper produtoMapper;
	
	@GetMapping
	@Cacheable(value = "listaProdutos")
	public List<ProdutoDTO> listar(@RequestParam(required = false) String nome) {
		List<Produto> produtos = null;
		if (nome == null) {
			produtos = produtoRepository.findAll();
		} else {
			produtos = produtoRepository.findByNome(nome);
		}
		
		List<ProdutoDTO> produtosDto = produtoMapper.toDto(produtos);
		return produtosDto;
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	@Secured({PerfilConstantes.ADMIN})
	public ResponseEntity<ProdutoDTO> cadastrar(@RequestBody @Valid ProdutoDTO produtoDto, UriComponentsBuilder uriBuilder) {
		Produto produto = produtoMapper.toEntity(produtoDto);
		produto = produtoRepository.save(produto);
		URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.getId()).toUri();
		return ResponseEntity.created(uri).body(produtoMapper.toDto(produto));
	}
	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoDTO produtoDto) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent()) {
			Produto produto = produtoRepository.save(produtoMapper.toEntity(produtoDto));
			return ResponseEntity.ok(produtoMapper.toDto(produto));
		}
		
		return ResponseEntity.notFound()
				.headers(HeaderUtil.createFailureAlert("PRODUTO", "PRODUTO_NAO_ENCONTRADO", "Produto não encontrado"))
				.build();
	}
	
	@GetMapping("/{id}")
	@Cacheable(value = "listaProdutos")
	public ResponseEntity<ProdutoDTO> obter(@PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isPresent()) {
			return ResponseEntity.ok(produtoMapper.toDto(produto.get()));
		}
		
		return ResponseEntity.notFound()
				.headers(HeaderUtil.createFailureAlert("PRODUTO", "PRODUTO_NAO_ENCONTRADO", "Produto não encontrado"))
				.build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	@Secured({PerfilConstantes.ADMIN})
	public ResponseEntity<ProdutoDTO> remover(@PathVariable Long id) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent()) {
			
			boolean produtoPossuiPedido = itemPedidoRepository.existsByProdutoId(id);
			
			if(produtoPossuiPedido) {
				return ResponseEntity.badRequest()
						.headers(HeaderUtil.createFailureAlert("PRODUTO", "PRODUTO_POSSUI_PEDIDO", "Produto não pode ser removido, pois possui pedidos efetuados"))
						.build();
			}
			
			produtoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound()
				.headers(HeaderUtil.createFailureAlert("PRODUTO", "PRODUTO_NAO_ENCONTRADO", "Produto não encontrado"))
				.build();
	}
}
