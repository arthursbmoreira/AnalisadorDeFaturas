package br.com.moreira.analisadordefaturas.filtro;


public class FiltroPlanilhaXL {
	private boolean numero = true;
	
	private boolean responsavel = false;
	
	private boolean ligacoes = false;
	
	private boolean mensagens= false;
	
	private boolean internet = false;
	
	private boolean custoServicosContratados = true;
	private boolean planos = false;
	private boolean descontos = false;
	private boolean valorBase = false;
	private boolean custoTotalFatura = true;
	
	public FiltroPlanilhaXL() {
	}

	public boolean isNumero() {
		return numero;
	}

	public void setNumero(boolean numero) {
		this.numero = numero;
	}

	public boolean isResponsavel() {
		return responsavel;
	}

	public void setResponsavel(boolean responsavel) {
		this.responsavel = responsavel;
	}

	public boolean isLigacoes() {
		return ligacoes;
	}

	public void setLigacoes(boolean ligacoes) {
		this.ligacoes = ligacoes;
	}

	public boolean isMensagens() {
		return mensagens;
	}

	public void setMensagens(boolean mensagens) {
		this.mensagens = mensagens;
	}

	public boolean isInternet() {
		return internet;
	}

	public void setInternet(boolean internet) {
		this.internet = internet;
	}

	public boolean isCustoServicosContratados() {
		return custoServicosContratados;
	}

	public void setCustoServicosContratados(boolean custoServicosContratados) {
		this.custoServicosContratados = custoServicosContratados;
	}
	
	public boolean isPlanos() {
		return planos;
	}

	public void setPlanos(boolean planos) {
		this.planos = planos;
	}

	public boolean isDescontos() {
		return descontos;
	}

	public void setDescontos(boolean descontos) {
		this.descontos = descontos;
	}
	
	public boolean isValorBase() {
		return valorBase;
	}

	public void setValorBase(boolean valorBase) {
		this.valorBase = valorBase;
	}

	public boolean isCustoTotalFatura() {
		return custoTotalFatura;
	}

	public void setCustoTotalFatura(boolean custoTotalFatura) {
		this.custoTotalFatura = custoTotalFatura;
	}
}
