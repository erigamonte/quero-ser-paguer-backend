package br.com.pague.desafio.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

import br.com.pague.desafio.controller.dto.ClienteDTO;
import br.com.pague.desafio.controller.mapper.ClienteMapper;
import br.com.pague.desafio.controller.util.HeaderUtil;
import br.com.pague.desafio.domain.Cliente;
import br.com.pague.desafio.repository.ClienteRepository;
import br.com.pague.desafio.repository.PedidoRepository;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/clientes")
@Api(tags = "Clientes")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private ClienteMapper clienteMapper;
	
	private HttpHeaders httpClienteNaoEncontrado = HeaderUtil.createFailureAlert("CLIENTE", "CLIENTE_NAO_ENCONTRADO", "Cliente não encontrado");

	private HttpHeaders httpCpfJaUtilizado = HeaderUtil.createFailureAlert("CLIENTE", "CPF_DUPLICADO", "CPF já utilizado por outro cliente");

	private HttpHeaders httpClientePossuiPedido = HeaderUtil.createFailureAlert("CLIENTE", "CLIENTE_POSSUI_PEDIDO", "Cliente não pode ser removido, pois possui pedidos efetuados");
	
	@GetMapping
	public List<ClienteDTO> listar(@RequestParam(required = false) String nome) {
		List<Cliente> clientes = null;
		if (nome == null) {
			clientes = clienteRepository.findAll();
		} else {
			clientes = clienteRepository.findAllByNome(nome);
		}
		return clienteMapper.toDto(clientes);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<ClienteDTO> cadastrar(@RequestBody @Valid ClienteDTO clienteDto, UriComponentsBuilder uriBuilder) {
		Cliente cliente = clienteMapper.toEntity(clienteDto);
		Optional<Cliente> optional = clienteRepository.findByCpf(cliente.getCpf());
		if(!optional.isPresent()) {
			cliente = clienteRepository.save(cliente);
			URI uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();
			return ResponseEntity.created(uri).body(clienteMapper.toDto(cliente));
		}
		
		return ResponseEntity.badRequest()
				.headers(httpCpfJaUtilizado)
				.body(clienteDto);
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ClienteDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteDTO clienteDto) {
		Optional<Cliente> optional = clienteRepository.findById(id);
		if (optional.isPresent()) {
			
			if(!optional.get().getCpf().equals(clienteDto.getCpf())) {
				Optional<Cliente> optionalClienteCpf = clienteRepository.findByCpf(clienteDto.getCpf());
				if(optionalClienteCpf.isPresent()) {
					return ResponseEntity.badRequest()
							.headers(httpCpfJaUtilizado)
							.body(clienteDto);
				}
			}
			
			Cliente cliente = clienteRepository.save(clienteMapper.toEntity(clienteDto));
			return ResponseEntity.ok(clienteMapper.toDto(cliente));
		}
		
		return ResponseEntity.notFound()
				.headers(httpClienteNaoEncontrado)
				.build();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ClienteDTO> obter(@PathVariable Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if (cliente.isPresent()) {
			return ResponseEntity.ok(clienteMapper.toDto(cliente.get()));
		}
		
		return ResponseEntity.notFound()
				.headers(httpClienteNaoEncontrado)
				.build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<ClienteDTO> remover(@PathVariable Long id) {
		Optional<Cliente> optional = clienteRepository.findById(id);
		if (optional.isPresent()) {
			
			boolean clientePossuiPedido = pedidoRepository.existsByClienteId(id);
			
			if(clientePossuiPedido) {
				return ResponseEntity.badRequest()
						.headers(httpClientePossuiPedido)
						.build();
			}

			clienteRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound()
				.headers(httpClienteNaoEncontrado)
				.build();
	}
}
