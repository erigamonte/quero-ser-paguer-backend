package br.com.pague.desafio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pague.desafio.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	List<Cliente> findByNome(String nome);

}
