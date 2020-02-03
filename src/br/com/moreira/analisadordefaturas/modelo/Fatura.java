package br.com.moreira.analisadordefaturas.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class Fatura implements Serializable {
	
	private static final long serialVersionUID = 3986018347655701236L;
	
	private Linha linha;
	private Ligacoes ligacoes;
	private Mensagens mensagens;
	private Internet internet;
	private BigDecimal custoServicosContratados = new BigDecimal("0.00");
	private BigDecimal custoTotalFatura = new BigDecimal("0.00");
	private BigDecimal totalVerificacao = new BigDecimal("0.00");
	private boolean isTotalCorreto = true;
	private BigDecimal descontos = new BigDecimal("0.00");
	private BigDecimal custoTotalFaturaComDesconto = new BigDecimal("0.00");
	private BigDecimal valorBase = new BigDecimal("0.00");
	private BigDecimal custoTotalFaturaPersonalizada = new BigDecimal("0.00");
	private BigDecimal custoTotalFaturaComDescontoPersonalizada = new BigDecimal("0.00");
	private BigDecimal custoTotalPlanos = new BigDecimal("0.00");
	private BigDecimal valorSubstituicao = new BigDecimal("0.00");
	
	public Fatura() {
	}
	
	public Fatura(Ligacoes ligacoes, Mensagens mensagens, Internet internet) {
		super();
		this.ligacoes = ligacoes;
		this.mensagens = mensagens;
		this.internet = internet;
	}

	public Fatura(Linha linha, Ligacoes ligacoes,
			Mensagens mensagens, Internet internet, BigDecimal valorSubstituicao,
			BigDecimal custoServicosContratados, BigDecimal totalVerificacao,
			BigDecimal descontos) {
		super();
		this.linha = linha;
		this.ligacoes = ligacoes;
		this.mensagens = mensagens;
		this.internet = internet;
		this.valorSubstituicao = valorSubstituicao;
		this.custoServicosContratados = custoServicosContratados;
		this.totalVerificacao = totalVerificacao;
		this.descontos = descontos;
		
		this.custoTotalFatura = this.custoTotalFatura.add(ligacoes.getCustoLigacoes())
													 .add(mensagens.getCustoMensagens())
													 .add(internet.getCustoUsoDeIternet())
													 .add(custoServicosContratados);
		
		this.custoTotalFaturaComDesconto = custoTotalFatura.subtract(descontos);
		
		this.isTotalCorreto = custoTotalFatura.compareTo(this.totalVerificacao) == 0 ? true : false;
	}

	public Linha getLinha() {
		return linha;
	}

	public void setLinha(Linha linha) {
		this.linha = linha;
	}

	public Ligacoes getLigacoes() {
		return ligacoes;
	}

	public void setLigacoes(Ligacoes ligacoes) {
		this.ligacoes = ligacoes;
	}

	public Mensagens getMensagens() {
		return mensagens;
	}

	public void setMensagens(Mensagens mensagens) {
		this.mensagens = mensagens;
	}

	public Internet getInternet() {
		return internet;
	}

	public void setInternet(Internet internet) {
		this.internet = internet;
	}
	
	public BigDecimal getValorSubstituicao() {
		return valorSubstituicao;
	}

	public BigDecimal getCustoServicosContratados() {
		return custoServicosContratados;
	}

	public void setCustoServicosContratados(BigDecimal custoServicosContratados) {
		this.custoServicosContratados = custoServicosContratados;
	}

	public BigDecimal getCustoTotalFatura() {
		return custoTotalFatura;
	}

	public void setCustoTotalFatura(BigDecimal custoTotalFatura) {
		this.custoTotalFatura = custoTotalFatura;
	}
	
	public BigDecimal getTotalVerificacao() {
		return totalVerificacao;
	}

	public void setTotalVerificacao(BigDecimal totalVerificacao) {
		this.totalVerificacao = totalVerificacao;
	}
	
	public boolean isTotalCorreto() {
		return isTotalCorreto;
	}

	public void setTotalCorreto(boolean isTotalCorreto) {
		this.isTotalCorreto = isTotalCorreto;
	}

	public BigDecimal getDescontos() {
		return descontos;
	}

	public void setDescontos(BigDecimal descontos) {
		this.descontos = descontos;
	}

	public BigDecimal getCustoTotalFaturaComDesconto() {
		return custoTotalFaturaComDesconto;
	}

	public void setCustoTotalFaturaComDesconto(BigDecimal custoTotalFaturaComDesconto) {
		this.custoTotalFaturaComDesconto = custoTotalFaturaComDesconto;
	}

	public BigDecimal getValorBase() {
		return valorBase;
	}

	public void setValorBase(BigDecimal valorBase) {
		this.valorBase = valorBase;
	}

	public BigDecimal getCustoTotalFaturaPersonalizada() {
		return custoTotalFaturaPersonalizada;
	}

	public void setCustoTotalFaturaPersonalizada(BigDecimal custoTotalFaturaPersonalizada) {
		this.custoTotalFaturaPersonalizada = custoTotalFaturaPersonalizada;
	}

	public BigDecimal getCustoTotalFaturaComDescontoPersonalizada() {
		return custoTotalFaturaComDescontoPersonalizada;
	}

	public void setCustoTotalFaturaComDescontoPersonalizada(BigDecimal custoTotalFaturaComDescontoPersonalizada) {
		this.custoTotalFaturaComDescontoPersonalizada = custoTotalFaturaComDescontoPersonalizada;
	}

	public BigDecimal getCustoTotalPlanos() {
		return custoTotalPlanos;
	}

	public void setCustoTotalPlanos(BigDecimal custoTotalPlanos) {
		this.custoTotalPlanos = custoTotalPlanos;
	}
	
	public BigDecimal getSomaServicos() {
		return this.custoServicosContratados.add(this.valorBase);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((linha == null) ? 0 : linha.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fatura other = (Fatura) obj;
		if (linha == null) {
			if (other.linha != null)
				return false;
		} else if (!linha.equals(other.linha))
			return false;
		return true;
	}
}
