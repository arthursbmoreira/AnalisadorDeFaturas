package br.com.moreira.analisadordefaturas.exceptions;

public class ArquivoInvalidoException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArquivoInvalidoException() {
		super("O arquivo selecionado não corresponde a um arquivo de fatura válido.");
	}
}
