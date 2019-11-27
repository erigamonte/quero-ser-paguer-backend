package br.com.pague.desafio.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pague.desafio.controller.error.DesafioException;
import br.com.pague.desafio.domain.Cliente;
import br.com.pague.desafio.repository.ClienteRepository;
import br.com.pague.desafio.repository.PedidoRepository;
import br.com.pague.desafio.service.ClienteService;
import br.com.pague.desafio.service.dto.ClienteDTO;
import br.com.pague.desafio.service.mapper.ClienteMapper;

@Service
public class ClienteServiceImpl implements ClienteService{
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ClienteMapper clienteMapper;

	@Override
	@Transactional
	public ClienteDTO salva(ClienteDTO dto) throws DesafioException {
		Cliente cliente = clienteMapper.toEntity(dto);
		Optional<Cliente> optional = clienteRepository.findByCpf(cliente.getCpf());
		if(!optional.isPresent()) {
			cliente = clienteRepository.save(cliente);
			return clienteMapper.toDto(cliente);
		}

		throw new DesafioException("CPF_JA_UTILIZADO", "CPF já utilizado por outro cliente");
	}

	@Override
	@Transactional
	public ClienteDTO atualiza(ClienteDTO dto) throws DesafioException {
		Optional<Cliente> optional = clienteRepository.findById(dto.getId());
		if (optional.isPresent()) {
			if(!optional.get().getCpf().equals(dto.getCpf())) {
				Optional<Cliente> optionalClienteCpf = clienteRepository.findByCpf(dto.getCpf());
				if(optionalClienteCpf.isPresent()) {
					throw new DesafioException("CPF_DUPLICADO", "CPF já utilizado por outro cliente");
				}
			}
			
			Cliente cliente = clienteRepository.save(clienteMapper.toEntity(dto));
			return clienteMapper.toDto(cliente);
		}
		
		throw new DesafioException("CLIENTE_NAO_ENCONTRADO", "Cliente não encontrado");
	}
	
	@Override
	public List<ClienteDTO> obtemTodos() {
		return obtemTodos(null);
	}

	@Override
	public List<ClienteDTO> obtemTodos(String nome) {
		List<Cliente> clientes = null;
		if (nome == null) {
			clientes = clienteRepository.findAll();
		} else {
			clientes = clienteRepository.findAllByNome(nome);
		}
		return clienteMapper.toDto(clientes);
	}

	@Override
	public ClienteDTO obtemPeloId(Long id) {
		Optional<Cliente> optional = clienteRepository.findById(id);
		if (optional.isPresent()) {
			return clienteMapper.toDto(optional.get());
		}
		
		return null;
	}

	@Override
	@Transactional
	public void remove(Long id) throws DesafioException {
		Optional<Cliente> optional = clienteRepository.findById(id);
		if (optional.isPresent()) {
			
			boolean clientePossuiPedido = pedidoRepository.existsByClienteId(id);
			
			if(clientePossuiPedido) {
				throw new DesafioException("CLIENTE_POSSUI_PEDIDO", "Cliente não pode ser removido, pois possui pedidos efetuados");
			}

			clienteRepository.deleteById(id);
		} else {
			throw new DesafioException("CLIENTE_NAO_ENCONTRADO", "Cliente não encontrado");
		}
		
	}

}
