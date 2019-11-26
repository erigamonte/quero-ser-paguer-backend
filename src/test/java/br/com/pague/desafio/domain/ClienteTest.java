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

import br.com.pague.desafio.builder.ClienteDTOBuilder;
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
		ClienteDTO cliente = ClienteDTOBuilder.umClienteDTO().comNome(null).constroi();
		
		//acao
		Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        
		//verificacao
		assertFalse(violations.isEmpty());		
	}
	
	@Test
	public void verificaClienteComCpfNulo() {
		//cenario
		ClienteDTO cliente = ClienteDTOBuilder.umClienteDTO().comCpf(null).constroi();
		
		//acao
		Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        
		//verificacao
		assertFalse(violations.isEmpty());		
	}
	
	@Test
	public void verificaClienteSemProblemasDeValidacao() {
		//cenario
		ClienteDTO cliente = ClienteDTOBuilder.umClienteDTO().constroi();
		
		//acao
		Set<ConstraintViolation<ClienteDTO>> violations = validator.validate(cliente);
        
		//verificacao
		assertTrue(violations.isEmpty());		
	}
}