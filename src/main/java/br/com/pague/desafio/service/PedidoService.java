package br.com.pague.desafio.service;

import java.util.List;

import br.com.pague.desafio.controller.error.DesafioException;
import br.com.pague.desafio.service.dto.PedidoDTO;

public interface PedidoService {
	
	PedidoDTO salva(PedidoDTO dto) throws DesafioException;
	
	PedidoDTO atualiza(PedidoDTO dto) throws DesafioException;
	
	List<PedidoDTO> obtemTodos();
	
	List<PedidoDTO> obtemTodos(Long clienteId);
	
	PedidoDTO obtemPeloId(Long id);
	
	void remove(Long id) throws DesafioException;
}
