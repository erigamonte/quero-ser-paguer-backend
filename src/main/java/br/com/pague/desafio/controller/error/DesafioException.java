package br.com.pague.desafio.controller.error;

import lombok.Data;

@Data
public class DesafioException extends Exception {
	private String code;

	public DesafioException(String code, String message) {
        super(message);
		this.code = code;
    }
}
