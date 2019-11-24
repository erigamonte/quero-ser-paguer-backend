package br.com.pague.desafio.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false)
	private Cliente cliente;
	
	@NotNull @NotEmpty
	@OneToMany(mappedBy = "pedido", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
	private List<ItemPedido> itemPedidos = new ArrayList<>();
	
	public BigDecimal getValorPedido() {
		BigDecimal total = BigDecimal.ZERO;
		itemPedidos.forEach(i -> total.add(i.getPreco()));
		return total;
	}
}
