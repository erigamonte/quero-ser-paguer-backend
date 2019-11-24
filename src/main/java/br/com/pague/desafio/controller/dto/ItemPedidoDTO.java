package br.com.pague.desafio.controller.dto;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import br.com.pague.desafio.controller.validation.PedidoValido;
import br.com.pague.desafio.controller.validation.ProdutoValido;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemPedidoDTO {
	private Long id;
	
	@NotNull @PedidoValido
	private Long pedidoId;
	
	@NotNull @ProdutoValido
	private Long produtoId;
	
	@NotNull @Min(value = 0)
	private BigDecimal preco;
	
	@NotNull @Min(value = 1)
	private BigDecimal quantidade;
}
