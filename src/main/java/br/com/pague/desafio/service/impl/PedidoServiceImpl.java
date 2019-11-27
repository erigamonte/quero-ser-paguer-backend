package br.com.pague.desafio.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pague.desafio.controller.error.DesafioException;
import br.com.pague.desafio.domain.Pedido;
import br.com.pague.desafio.repository.PedidoRepository;
import br.com.pague.desafio.service.PedidoService;
import br.com.pague.desafio.service.dto.PedidoDTO;
import br.com.pague.desafio.service.mapper.PedidoMapper;

@Service
public class PedidoServiceImpl implements PedidoService{
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PedidoMapper pedidoMapper;

	@Override
	@Transactional
	public PedidoDTO salva(PedidoDTO dto) throws DesafioException {
		Pedido pedido = pedidoMapper.toEntity(dto);
		pedido = pedidoRepository.save(pedido);
		return pedidoMapper.toDto(pedido);
	}

	@Override
	@Transactional
	public PedidoDTO atualiza(PedidoDTO dto) throws DesafioException {
		Optional<Pedido> optional = pedidoRepository.findById(dto.getId());
		if (optional.isPresent()) {
			Pedido pedido = pedidoRepository.save(pedidoMapper.toEntity(dto));
			return pedidoMapper.toDto(pedido);
		}
		
		throw new DesafioException("PEDIDO_NAO_ENCONTRADO", "Pedido não encontrado");
	}
	
	@Override
	public List<PedidoDTO> obtemTodos() {
		return obtemTodos(null);
	}

	@Override
	public List<PedidoDTO> obtemTodos(Long clienteId) {
		List<Pedido> pedidos = null;
		if (clienteId == null) {
			pedidos = pedidoRepository.findAll();
		} else {
			pedidos = pedidoRepository.findAllByClienteId(clienteId);
		}
		return pedidoMapper.toDto(pedidos);
	}

	@Override
	public PedidoDTO obtemPeloId(Long id) {
		Optional<Pedido> optional = pedidoRepository.findById(id);
		if (optional.isPresent()) {
			return pedidoMapper.toDto(optional.get());
		}
		
		return null;
	}

	@Override
	@Transactional
	public void remove(Long id) throws DesafioException {
		Optional<Pedido> optional = pedidoRepository.findById(id);
		if (optional.isPresent()) {
			pedidoRepository.deleteById(id);
		} else {
			throw new DesafioException("PEDIDO_NAO_ENCONTRADO", "Pedido não encontrado");
		}
	}

}
