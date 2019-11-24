package br.com.pague.desafio.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Cliente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull @NotEmpty @Length(min=5, max=100)
	@Column(length = 100, nullable = false)
	private String nome;
	
	@NotNull @NotEmpty @Length(min=11, max=11) @CPF
	@Column(length = 11, nullable = false, unique = true)
	private String cpf;
	
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date dataNascimento;
}
