package br.com.pague.desafio.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Perfil implements GrantedAuthority {
	
	private static final long serialVersionUID = 1L;

	@Id
	private String nome;

	@Override
	public String getAuthority() {
		return nome;
	}
	
}
