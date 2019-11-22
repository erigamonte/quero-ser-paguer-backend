package br.com.pague.desafio.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
//		if (nome == null) {
//			List<Produto> topicos = produtoRepository.findAll();
//			return topicos;
//		} else {
//			List<Produto> topicos = produtoRepository.findByNome(nome);
//			return topicos;
//		}
		
		List<Produto> listaProdutos = new ArrayList<Produto>();
		Produto p = new Produto(null, "Bala", BigDecimal.TEN);
		listaProdutos.add(p);
		p = new Produto(null, "Bala 2", BigDecimal.TEN);
		listaProdutos.add(p);
		p = new Produto(null,  "Bala 3", BigDecimal.TEN);
		listaProdutos.add(p);
		
		return listaProdutos;
		
	}
	
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	public void cadastrar() {
		
	}
	
	@PutMapping
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	public void atualizar() {
		
	}
	
	@GetMapping("/{id}")
	@Cacheable(value = "listaProdutos")
	public void obter() {
		
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	@CacheEvict(value = "listaProdutos", allEntries = true)
	public void remover() {
			
	}
}
