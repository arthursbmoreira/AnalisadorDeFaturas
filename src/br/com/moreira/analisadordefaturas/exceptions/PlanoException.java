package br.com.moreira.analisadordefaturas.exceptions;

public class PlanoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PlanoException(String erro) {
		super(erro);
	}
	
	public PlanoException(int linhasArquivo, int planoCliente) {
		super("O arquivo selecionado não condiz com o seu plano. O arquivo possui " + linhasArquivo +" linhas, e seu plano possui " + planoCliente + " linhas.");
	}

}
