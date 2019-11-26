package br.com.pague.desafio.service;

import java.util.List;

import br.com.pague.desafio.controller.error.DesafioException;
import br.com.pague.desafio.service.dto.ClienteDTO;

public interface ClienteService {
	
	ClienteDTO salva(ClienteDTO dto) throws DesafioException;
	
	ClienteDTO atualiza(ClienteDTO dto) throws DesafioException;
	
	List<ClienteDTO> obtemTodos();
	
	List<ClienteDTO> obtemTodos(String nome);
	
	ClienteDTO obtemPeloId(Long id);
	
	void remove(Long id) throws DesafioException;
}
