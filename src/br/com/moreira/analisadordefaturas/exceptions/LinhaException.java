package br.com.moreira.analisadordefaturas.exceptions;

public class LinhaException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LinhaException(String erro) {
		super(erro);
	}
}
