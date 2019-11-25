package br.com.pague.desafio.controller;

import static br.com.pague.desafio.builder.ClienteBuilder.umCliente;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.pague.desafio.controller.dto.ClienteDTO;
import br.com.pague.desafio.domain.Cliente;
import br.com.pague.desafio.repository.ClienteRepository;

@RunWith(SpringRunner.class)
public class ClienteControllerTest {

	@Autowired
	private ClienteController clienteController;
	
	@MockBean
	private ClienteRepository repository;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}	
	
	@Test
	public void deveEncontrarUmCliente() {
		//cenario
		Cliente cliente = umCliente()
							.comCpf("11724436708")
							.comNome("João")
							.constroi();
		
		Optional<Cliente> expectedReturn =  Optional.of(cliente);
		when(repository.findById(1l)).thenReturn(expectedReturn);
		
		//ação
		ResponseEntity<ClienteDTO> response = clienteController.obter(1l);
		
		//verificação
		assertEquals(response.getStatusCodeValue(), 200);
		assertEquals(cliente, response.getBody());
	}
}
