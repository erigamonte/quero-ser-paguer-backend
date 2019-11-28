package br.com.pague.desafio.controller;

import java.net.URI;
import java.util.List;

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
import br.com.pague.desafio.controller.error.DesafioException;
import br.com.pague.desafio.controller.util.HeaderUtil;
import br.com.pague.desafio.service.ProdutoService;
import br.com.pague.desafio.service.dto.ProdutoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/produtos")
@Api(tags = "Produtos")
public class ProdutoController {
	
	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping
	@Cacheable(value = "listaProdutos")
	@ApiOperation(value = "Buscar Produtos", notes = "Buscar Produtos")
	public List<ProdutoDTO> listar(@RequestParam(required = false) String nome) {
		return produtoService.obtemTodos(nome);
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	@Secured({PerfilConstantes.ADMIN})
	@ApiOperation(value = "Cadastrar Produto", notes = "Cadastrar Produto")
	public ResponseEntity<ProdutoDTO> cadastrar(@RequestBody @Valid ProdutoDTO produtoDto, UriComponentsBuilder uriBuilder) {
		ProdutoDTO produto;
		try {
			produto = produtoService.salva(produtoDto);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("PRODUTO", e.getCode(), e.getMessage()))
					.body(produtoDto);
		}
		URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.getId()).toUri();
		return ResponseEntity.created(uri).body(produto);
	}
	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	@ApiOperation(value = "Atualizar Produto", notes = "Atualizar Produto")
	public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoDTO produtoDto) {
		ProdutoDTO produto;
		try {
			produtoDto.setId(id);
			produto = produtoService.salva(produtoDto);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("PRODUTO", e.getCode(), e.getMessage()))
					.body(produtoDto);
		}
			
		return ResponseEntity.ok(produto);
	}
	
	@GetMapping("/{id}")
	@Cacheable(value = "listaProdutos")
	@ApiOperation(value = "Detalhar Produto", notes = "Detalhar Produto")
	public ResponseEntity<ProdutoDTO> obter(@PathVariable Long id) {
		ProdutoDTO produto = produtoService.obtemPeloId(id);
		if (produto != null) {
			return ResponseEntity.ok(produto);
		}
		
		return ResponseEntity.notFound()
				.headers(HeaderUtil.createFailureAlert("PRODUTO", "PRODUTO_NAO_ENCONTRADO", "Produto não encontrado"))
				.build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	@Secured({PerfilConstantes.ADMIN})
	@ApiOperation(value = "Remover Produto", notes = "Remover Produto")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		try {
			produtoService.remove(id);
		} catch (DesafioException e) {
			return ResponseEntity.badRequest()
					.headers(HeaderUtil.createFailureAlert("PRODUTO", e.getCode(), e.getMessage()))
					.body(null);
		}
		
		return ResponseEntity.noContent().build();
	}
}
