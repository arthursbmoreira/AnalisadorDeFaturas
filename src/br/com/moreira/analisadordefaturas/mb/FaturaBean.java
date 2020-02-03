package br.com.moreira.analisadordefaturas.mb;

import java.math.BigDecimal;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.moreira.analisadordefaturas.modelo.Fatura;
import br.com.moreira.analisadordefaturas.modelo.Internet;
import br.com.moreira.analisadordefaturas.modelo.Ligacoes;
import br.com.moreira.analisadordefaturas.modelo.Mensagens;

@ManagedBean
@SessionScoped
public class FaturaBean {
	
	private Fatura fatura = new Fatura();
	private Ligacoes ligacoes = new Ligacoes();
	private Mensagens mensagens = new Mensagens();
	private Internet internet = new Internet();
	private String responsavel = "";
	private BigDecimal custoServicosContratados = BigDecimal.ZERO;
	private BigDecimal descontos = BigDecimal.ZERO;
	private BigDecimal custoTotalFatura = BigDecimal.ZERO;
	private BigDecimal custoTotalFaturaComDesconto = BigDecimal.ZERO;
	
	public FaturaBean() {
		
	}
	
	public void visualizarFatura(Fatura fatura) {
		this.responsavel = fatura.getLinha().getResponsavel();
		this.ligacoes = fatura.getLigacoes();
		this.mensagens = fatura.getMensagens();
		this.internet = fatura.getInternet();
		this.custoServicosContratados = fatura.getCustoServicosContratados();
		this.descontos = fatura.getDescontos();
		this.custoTotalFatura = fatura.getCustoTotalFatura();
		this.custoTotalFaturaComDesconto = custoTotalFatura.subtract(descontos);
	}
	
	public Fatura getFatura() {
		return fatura;
	}

	public Ligacoes getLigacoes() {
		return ligacoes;
	}

	public Mensagens getMensagens() {
		return mensagens;
	}

	public Internet getInternet() {
		return internet;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public BigDecimal getCustoServicosContratados() {
		return custoServicosContratados;
	}
	
	public BigDecimal getDescontos() {
		return descontos;
	}

	public BigDecimal getCustoTotalFatura() {
		return custoTotalFatura;
	}

	public BigDecimal getCustoTotalFaturaComDesconto() {
		return custoTotalFaturaComDesconto;
	}
	
	
}
