package br.com.pague.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pague.desafio.domain.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
