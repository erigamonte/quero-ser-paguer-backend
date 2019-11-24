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

import br.com.pague.desafio.domain.Produto;
import br.com.pague.desafio.repository.ProdutoRepository;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	@Cacheable(value = "listaProdutos")
	public List<Produto> listar(@RequestParam(required = false) String nome) {
		List<Produto> produtos = null;
		if (nome == null) {
			produtos = produtoRepository.findAll();
		} else {
			produtos = produtoRepository.findByNome(nome);
		}
		return produtos;
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	public ResponseEntity<Produto> cadastrar(@RequestBody @Valid Produto produto, UriComponentsBuilder uriBuilder) {
		produtoRepository.save(produto);
		URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(produto.getId()).toUri();
		return ResponseEntity.created(uri).body(produto);
	}
	
	@PutMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody @Valid Produto form) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent()) {
			Produto produto = optional.get();
			
			produto.setNome(form.getNome());
			produto.setPrecoSugerido(form.getPrecoSugerido());
			
			return ResponseEntity.ok(produto);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/{id}")
	@Cacheable(value = "listaProdutos")
	public ResponseEntity<Produto> obter(@PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		if (produto.isPresent()) {
			return ResponseEntity.ok(produto.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	public ResponseEntity<Produto> remover(@PathVariable Long id) {
		Optional<Produto> optional = produtoRepository.findById(id);
		if (optional.isPresent()) {
			produtoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
