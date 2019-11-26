package br.com.pague.desafio.service.dto.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.pague.desafio.repository.ProdutoRepository;

public class ProdutoValidator implements ConstraintValidator<ProdutoValido, Long> {

	@Autowired
	private ProdutoRepository produtoRepository;

	@Override
	public boolean isValid(Long value, ConstraintValidatorContext context) {
		return value != null && produtoRepository.findById(value).isPresent();
	}
	
}