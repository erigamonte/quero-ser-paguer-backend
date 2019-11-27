package br.com.pague.desafio.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name="pedido")
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false)
	private Cliente cliente;
	
	@OneToMany(mappedBy = "pedido", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
	private List<ItemPedido> itemPedidos = new ArrayList<>();
	
	public BigDecimal getValorPedido() {
		BigDecimal total = BigDecimal.ZERO;
		total = itemPedidos.stream().map(item-> item.getPreco().multiply(item.getQuantidade())).reduce(BigDecimal.ZERO,BigDecimal::add);
		return total;
	}
}
