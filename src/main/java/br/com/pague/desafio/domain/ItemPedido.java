package br.com.pague.desafio.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ItemPedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne(optional=false)
	private Pedido pedido;
	
	@NotNull
	@ManyToOne(optional=false)
	private Produto produto;
	
	@NotNull @Min(value = 0)
	@Column(precision = 10, scale = 2)
	private BigDecimal preco;
	
	@NotNull @Min(value = 1)
	@Column(precision = 10, scale = 2)
	private BigDecimal quantidade;
}
