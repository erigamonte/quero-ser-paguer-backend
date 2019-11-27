package br.com.pague.desafio.service;

import java.util.List;

import br.com.pague.desafio.controller.error.DesafioException;
import br.com.pague.desafio.service.dto.ProdutoDTO;

public interface ProdutoService {
	
	ProdutoDTO salva(ProdutoDTO dto) throws DesafioException;
	
	ProdutoDTO atualiza(ProdutoDTO dto) throws DesafioException;
	
	List<ProdutoDTO> obtemTodos();
	
	List<ProdutoDTO> obtemTodos(String nome);
	
	ProdutoDTO obtemPeloId(Long id);
	
	void remove(Long id) throws DesafioException;
}
