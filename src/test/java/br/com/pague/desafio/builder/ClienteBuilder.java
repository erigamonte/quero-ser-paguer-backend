package br.com.pague.desafio.builder;

import java.util.Date;

import br.com.pague.desafio.domain.Cliente;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteBuilder {

	private Cliente cliente;
	
	public static ClienteBuilder umCliente() {
		ClienteBuilder builder = new ClienteBuilder();
		builder.cliente = new Cliente();
		builder.cliente.setNome("Cliente 1");
		builder.cliente.setCpf("11111111111");
		builder.cliente.setDataNascimento(new Date());
		return builder;
	}
	
	public ClienteBuilder comNome(String nome) {
		cliente.setNome(nome);
		return this;
	}
	
	public ClienteBuilder comCpf(String cpf) {
		cliente.setCpf(cpf);
		return this;
	}
	
	public Cliente constroi() {
		return this.cliente;
	}
	
}
