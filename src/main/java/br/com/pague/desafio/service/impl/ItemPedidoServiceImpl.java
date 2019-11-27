package br.com.pague.desafio.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pague.desafio.controller.error.DesafioException;
import br.com.pague.desafio.domain.ItemPedido;
import br.com.pague.desafio.domain.Produto;
import br.com.pague.desafio.repository.ItemPedidoRepository;
import br.com.pague.desafio.repository.ProdutoRepository;
import br.com.pague.desafio.service.ItemPedidoService;
import br.com.pague.desafio.service.dto.ItemPedidoDTO;
import br.com.pague.desafio.service.mapper.ItemPedidoMapper;

@Service
public class ItemPedidoServiceImpl implements ItemPedidoService{
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoMapper itemPedidoMapper;

	@Override
	@Transactional
	public ItemPedidoDTO salva(ItemPedidoDTO dto) throws DesafioException {
		Produto produto = produtoRepository.getOne(dto.getProdutoId());
		dto.setPreco(produto.getPrecoSugerido());
		ItemPedido itemPedido = itemPedidoMapper.toEntity(dto);
		itemPedido = itemPedidoRepository.save(itemPedido);
		return itemPedidoMapper.toDto(itemPedido);
	}

	@Override
	@Transactional
	public ItemPedidoDTO atualiza(ItemPedidoDTO dto) throws DesafioException {
		Optional<ItemPedido> optional = itemPedidoRepository.findById(dto.getId());
		if (optional.isPresent()) {
			ItemPedido itemPedido = itemPedidoRepository.save(itemPedidoMapper.toEntity(dto));
			return itemPedidoMapper.toDto(itemPedido);
		}
		
		throw new DesafioException("ITEM_PEDIDO_NAO_ENCONTRADO", "Item de Pedido não encontrado");
	}
	
	@Override
	public List<ItemPedidoDTO> obtemTodos() {
		return obtemTodos(null);
	}

	@Override
	public List<ItemPedidoDTO> obtemTodos(Long pedidoId) {
		List<ItemPedido> itemPedidos = null;
		if (pedidoId == null) {
			itemPedidos = itemPedidoRepository.findAll();
		} else {
			itemPedidos = itemPedidoRepository.findAllByPedidoId(pedidoId);
		}
		return itemPedidoMapper.toDto(itemPedidos);
	}

	@Override
	public ItemPedidoDTO obtemPeloId(Long id) {
		Optional<ItemPedido> optional = itemPedidoRepository.findById(id);
		if (optional.isPresent()) {
			return itemPedidoMapper.toDto(optional.get());
		}
		
		return null;
	}

	@Override
	@Transactional
	public void remove(Long id) throws DesafioException {
		Optional<ItemPedido> optional = itemPedidoRepository.findById(id);
		if (optional.isPresent()) {
			itemPedidoRepository.deleteById(id);
		} else {
			throw new DesafioException("ITEM_PEDIDO_NAO_ENCONTRADO", "Item de Pedido não encontrado");
		}
	}

}
