package br.com.pague.desafio.service.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import br.com.pague.desafio.service.dto.validation.ClienteValido;
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
