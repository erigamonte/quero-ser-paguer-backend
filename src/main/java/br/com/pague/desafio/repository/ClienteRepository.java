package br.com.pague.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pague.desafio.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
