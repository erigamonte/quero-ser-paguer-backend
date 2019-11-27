package br.com.pague.desafio.service;

import java.util.List;

import br.com.pague.desafio.controller.error.DesafioException;
import br.com.pague.desafio.service.dto.ItemPedidoDTO;

public interface ItemPedidoService {
	
	ItemPedidoDTO salva(ItemPedidoDTO dto) throws DesafioException;
	
	ItemPedidoDTO atualiza(ItemPedidoDTO dto) throws DesafioException;
	
	List<ItemPedidoDTO> obtemTodos();
	
	List<ItemPedidoDTO> obtemTodos(Long pedidoId);
	
	ItemPedidoDTO obtemPeloId(Long id);
	
	void remove(Long id) throws DesafioException;
}
