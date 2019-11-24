package br.com.pague.desafio.config.security.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDTO {

	@NotNull @NotEmpty
	private String token;
	
	@NotNull @NotEmpty
	private String tipo;
}
