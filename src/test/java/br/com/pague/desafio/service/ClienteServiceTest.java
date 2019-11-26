package br.com.pague.desafio.service;

import static br.com.pague.desafio.builder.ClienteBuilder.umCliente;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.pague.desafio.repository.ClienteRepository;
import br.com.pague.desafio.service.dto.ClienteDTO;
import br.com.pague.desafio.service.impl.ClienteServiceImpl;

@RunWith(SpringRunner.class)
public class ClienteServiceTest {

	@Autowired
	private ClienteService clienteService;
	
	@MockBean
	private ClienteRepository clienteRepository;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}	
	
	@TestConfiguration
    static class ClienteServiceImplTestContextConfiguration {
        @Bean
        public ClienteService clienteService() {
            return new ClienteServiceImpl();
        }
    }
	
	@Test
	public void deveEncontrarUmCliente() {
		//cenario
		ClienteDTO cliente = umCliente()
							.comCpf("11724436708")
							.comNome("João")
							.constroi();
		
		ClienteDTO expectedReturn =  cliente;
		
		when(clienteService.obtemPeloId(1l)).thenReturn(expectedReturn);
		
		//ação
		ClienteDTO clienteSalvo = clienteService.obtemPeloId(1l);
		
		//verificação
		verify(clienteService, times(1)).obtemPeloId(1l);
		assertEquals(cliente.getId(), clienteSalvo.getId());
	}
}
