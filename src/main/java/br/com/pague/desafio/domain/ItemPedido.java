package br.com.pague.desafio.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name="item_pedido")
public class ItemPedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional=false)
	private Pedido pedido;
	
	@ManyToOne(optional=false)
	private Produto produto;
	
	@Column(name = "preco", precision = 10, scale = 2, nullable = false)
	private BigDecimal preco;
	
	@Column(name = "quantidade", precision = 10, scale = 2, nullable = false)
	private BigDecimal quantidade;
}
