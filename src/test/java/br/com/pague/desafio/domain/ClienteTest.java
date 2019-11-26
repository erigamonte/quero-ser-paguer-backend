package br.com.pague.desafio.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.pague.desafio.builder.ClienteBuilder;
import br.com.pague.desafio.service.dto.ClienteDTO;

@RunWith(SpringRunner.class)
public class ClienteTest {

	private Validator validator;

	@Before
	public void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void verificaClienteComNomeNulo() {
		//cenario
		ClienteDTO cliente = ClienteBuilder.umCliente().comNome(null).constroi();
		
		//acao
		Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        
		//verificacao
		assertFalse(violations.isEmpty());		
	}
	
	@Test
	public void verificaClienteComCpfNulo() {
		//cenario
		ClienteDTO cliente = ClienteBuilder.umCliente().comCpf(null).constroi();
		
		//acao
		Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        
		//verificacao
		assertFalse(violations.isEmpty());		
	}
	
	@Test
	public void verificaClienteSemProblemasDeValidacao() {
		//cenario
		ClienteDTO cliente = ClienteBuilder.umCliente().constroi();
		
		//acao
		Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        
		//verificacao
		assertTrue(violations.isEmpty());		
	}
}