package br.com.pague.desafio.builder;

import java.util.Date;

import br.com.pague.desafio.domain.Cliente;
import br.com.pague.desafio.service.dto.ClienteDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteDTOBuilder {

	private ClienteDTO cliente;
	
	public static ClienteDTOBuilder umClienteDTO() {
		ClienteDTOBuilder builder = new ClienteDTOBuilder();
		builder.cliente = new ClienteDTO();
		builder.cliente.setNome("Joao da Silva");
		builder.cliente.setCpf("11724436708");
		builder.cliente.setDataNascimento(new Date());
		return builder;
	}
	
	public ClienteDTOBuilder comNome(String nome) {
		cliente.setNome(nome);
		return this;
	}
	
	public ClienteDTOBuilder comCpf(String cpf) {
		cliente.setCpf(cpf);
		return this;
	}
	
	public ClienteDTOBuilder comDataNascimento(Date data) {
		cliente.setDataNascimento(data);
		return this;
	}
	
	public ClienteDTO constroi() {
		return this.cliente;
	}
	
}
