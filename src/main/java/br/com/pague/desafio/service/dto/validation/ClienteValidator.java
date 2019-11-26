package br.com.pague.desafio.service.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.pague.desafio.repository.ClienteRepository;

public class ClienteValidator implements ConstraintValidator<ClienteValido, Long> {

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return value != null && clienteRepository.findById(value).isPresent();
	}
	
}