package br.com.pague.desafio.controller.form;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AtualizacaoItemPedidoForm {
	
	@NotNull @Min(value = 1)
	private BigDecimal quantidade;
}
