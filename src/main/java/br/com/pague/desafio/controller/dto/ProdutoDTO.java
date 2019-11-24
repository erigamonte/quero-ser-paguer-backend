package br.com.pague.desafio.controller.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
	private Long id;
	
	@NotNull @NotEmpty @Size(min=5, max=100)
	private String nome;
	
	@NotNull @Min(value = 0)
	private BigDecimal precoSugerido;
}
