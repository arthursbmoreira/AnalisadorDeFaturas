package br.com.moreira.analisadordefaturas.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String totalTempoLigacoes = "0m00s";
	private BigDecimal custoTotalFatura = new BigDecimal("0.00");
	private BigDecimal custoTotalAcimaDoContratado = new BigDecimal("0.00");
	private BigDecimal custoTotalPlanos = new BigDecimal("0.00");
	private BigDecimal descontos = new BigDecimal("0.00");
	private BigDecimal total = new BigDecimal("0.00");
	private BigDecimal totalVerificacao = new BigDecimal("0.00");
	private boolean isTotalCorreto = true;
	private BigDecimal outrosLancamentos = new BigDecimal("0.00");
	private BigDecimal deducoesPorLei = new BigDecimal("0.00");
	private int linhasUtilizadas = 0;
	private int linhasNaoUtilizadas = 0;
	private BigDecimal custoTotalFaturaPersonalizada = new BigDecimal("0.00");
	
	public Empresa() {
		
	}
	
	public Empresa(String totalTempoLigacoes, BigDecimal custoTotalFatura,
			BigDecimal custoTotalFaturaPersonalizada,
			BigDecimal custoTotalAcimaDoContratado,
			BigDecimal custoTotalPlanos, BigDecimal descontos,
			BigDecimal totalVerificacao,
			BigDecimal outrosLancamentos, BigDecimal deducoesPorLei,
			int linhasUtilizadas, int linhasNaoUtilizadas) {
		super();
		this.totalTempoLigacoes = totalTempoLigacoes;
		this.custoTotalFatura = custoTotalFatura;
		this.custoTotalFaturaPersonalizada = custoTotalFaturaPersonalizada;
		this.custoTotalAcimaDoContratado = custoTotalAcimaDoContratado;
		this.custoTotalPlanos = custoTotalPlanos;
		this.descontos = descontos;
		this.total = custoTotalFatura.subtract(descontos).add(outrosLancamentos).subtract(deducoesPorLei);
		this.totalVerificacao = totalVerificacao;
		this.outrosLancamentos = outrosLancamentos;
		this.deducoesPorLei = deducoesPorLei;
		this.linhasUtilizadas = linhasUtilizadas;
		this.linhasNaoUtilizadas = linhasNaoUtilizadas;
		
		isTotalCorreto = total.compareTo(totalVerificacao) == 0 ? true : false;
	}

	public String getTotalTempoLigacoes() {
		return totalTempoLigacoes;
	}

	public BigDecimal getCustoTotalFatura() {
		return custoTotalFatura;
	}

	public BigDecimal getCustoTotalAcimaDoContratado() {
		return custoTotalAcimaDoContratado;
	}

	public BigDecimal getCustoTotalPlanos() {
		return custoTotalPlanos;
	}

	public BigDecimal getDescontos() {
		return descontos;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public BigDecimal getTotalVerificacao() {
		return totalVerificacao;
	}
	
	public boolean isTotalCorreto() {
		return isTotalCorreto;
	}

	public BigDecimal getOutrosLancamentos() {
		return outrosLancamentos;
	}

	public BigDecimal getDeducoesPorLei() {
		return deducoesPorLei;
	}

	public int getLinhasUtilizadas() {
		return linhasUtilizadas;
	}

	public int getLinhasNaoUtilizadas() {
		return linhasNaoUtilizadas;
	}

	public BigDecimal getCustoTotalFaturaPersonalizada() {
		return custoTotalFaturaPersonalizada;
	}
	
	public BigDecimal getCustoTotalFaturaComDescontosPersonalizado() {
		return custoTotalFaturaPersonalizada.subtract(descontos).add(outrosLancamentos).subtract(deducoesPorLei);
	}
}
