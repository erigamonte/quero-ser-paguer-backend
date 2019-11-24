package br.com.pague.desafio.controller.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClienteDTO {
	private Long id;
	
	@NotNull @NotEmpty @Size(min=5, max=100)
	private String nome;
	
	@NotNull @NotEmpty @Size(min=11, max=11) @CPF
	private String cpf;
	
	@NotNull
	private Date dataNascimento;
}
