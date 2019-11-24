package br.com.pague.desafio.controller.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import br.com.pague.desafio.controller.validation.ClienteValido;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PedidoDTO {
	
	private Long id;
	
	@NotNull @ClienteValido
	private Long clienteId;
	
	private BigDecimal valorPedido;
	
}
