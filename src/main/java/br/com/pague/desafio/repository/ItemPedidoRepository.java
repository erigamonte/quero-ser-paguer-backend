package br.com.pague.desafio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pague.desafio.domain.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>{

	List<ItemPedido> findAllByPedidoId(Long pedidoId);

}
