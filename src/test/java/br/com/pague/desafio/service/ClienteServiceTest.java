package br.com.pague.desafio.service;

import static br.com.pague.desafio.builder.ClienteBuilder.umCliente;
import static br.com.pague.desafio.builder.ClienteDTOBuilder.umClienteDTO;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.pague.desafio.domain.Cliente;
import br.com.pague.desafio.repository.ClienteRepository;
import br.com.pague.desafio.repository.PedidoRepository;
import br.com.pague.desafio.service.dto.ClienteDTO;
import br.com.pague.desafio.service.impl.ClienteServiceImpl;
import br.com.pague.desafio.service.mapper.ClienteMapper;

@RunWith(SpringRunner.class)
public class ClienteServiceTest {

	@InjectMocks
	private ClienteService clienteService = new ClienteServiceImpl();
	
	@Mock
	private ClienteRepository clienteRepository;
	
	@Mock
	private PedidoRepository pedidoRepository;
	
	@Mock
	private ClienteMapper clienteMapper;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}	
	
	@Test
	public void deveEncontrarUmCliente() {
		//cenario
		ClienteDTO clienteDTO = umClienteDTO().constroi();
		Cliente cliente = umCliente().constroi();
		
		Optional<Cliente> optional = Optional.of(cliente);
		when(clienteRepository.findById(1l)).thenReturn(optional);
		when(clienteMapper.toDto(optional.get())).thenReturn(clienteDTO);
		
		//ação
		ClienteDTO clienteSalvo = clienteService.obtemPeloId(1l);
		
		//verificação
		verify(clienteRepository, times(1)).findById(1l);
		verify(clienteMapper, times(1)).toDto(optional.get());
		
		assertEquals(clienteDTO, clienteSalvo);
	}
}
