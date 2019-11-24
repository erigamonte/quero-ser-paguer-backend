package br.com.pague.desafio.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.pague.desafio.repository.PedidoRepository;

public class PedidoValidator implements ConstraintValidator<PedidoValido, Long> {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return value != null && pedidoRepository.findById(value).isPresent();
	}
	
}