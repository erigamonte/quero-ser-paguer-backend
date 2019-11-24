package br.com.pague.desafio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pague.desafio.domain.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

	List<Pedido> findAllByClienteId(Long clienteId);

	boolean existsByClienteId(Long clienteId);

}
