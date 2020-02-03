package br.com.moreira.analisadordefaturas.modelo;

import java.math.BigDecimal;
import java.util.List;

public class Ligacoes {
	private String tempoLigacoesPersonalizadas = "0m00s";
	private String tempoExcedenteLigacoesPersonalizadas = "0m";
	private BigDecimal custoLigacoesPersonalizadas = new BigDecimal("0.00");
	private BigDecimal custoLigacoesNaoIncluidas = new BigDecimal("0.00");
	
	private String tempoLigacoesLocais = "0m00s";
	private BigDecimal custoLigacoesLocais = new BigDecimal("0.00");
	
	private String tempoCaixaPostal = "0m00s";
	private BigDecimal custoCaixaPostal = new BigDecimal("0.00");

	private String tempoLigacoesIntragrupo = "0m00s";

	private String tempoLigacoesInterurbanas = "0m00s";
	private BigDecimal custoLigacoesInterurbanas = new BigDecimal("0.00");

	private String tempoLigacoesEmRoaming = "0m00s";
	private BigDecimal custoLigacoesEmRoaming = new BigDecimal("0.00");

	private String tempoLigacoesRecebidasACobrar = "0m00s";
	private BigDecimal custoLigacoesRecebidasACobrar = new BigDecimal("0.00");

	private String tempoLigacoesServicosTerceiros = "0m00s";
	private BigDecimal custoLigacoesServicosTerceiros = new BigDecimal("0.00");
	
	private int totalLojaDeServicosVivo = 0;
	private BigDecimal custoLojaDeServicosVivo = new BigDecimal("0.00");
	
	private String tempoLigacoesNoExterior = "0m00s";
	private BigDecimal custoLigacoesNoExterior = new BigDecimal("0.00");
	
	private String tempoLigacoes = "0m00s";
	private BigDecimal custoLigacoes = new BigDecimal("0.00");
	
	private BigDecimal adicionalPorLigacoes = new BigDecimal("0.00");

	private String tempoLigacoesDiariasVivoTravel = "0m00s";
	private BigDecimal custoDiariasVivoTravel = new BigDecimal("0.00");
	
	
	public Ligacoes() {
		
	}

	public Ligacoes(String tempoLigacoesLocais, BigDecimal custoLigacoesLocais,
			String tempoCaixaPostal, BigDecimal custoCaixaPostal,
			String tempoLigacoesIntragrupo, String tempoLigacoesInterurbanas,
			BigDecimal custoLigacoesInterurbanas,
			String tempoLigacoesEmRoaming, BigDecimal custoLigacoesEmRoaming,
			String tempoLigacoesRecebidasACobrar,
			BigDecimal custoLigacoesRecebidasACobrar,
			String tempoLigacoesServicosTerceiros,
			BigDecimal custoLigacoesServicosTerceiros,
			int totalLojaDeServicosVivo, BigDecimal custoLojaDeServicosVivo,
			String tempoLigacoesNoExterior, BigDecimal custoLigacoesNoExterior,
			BigDecimal adicionalPorLigacoes, 
			String tempoLigacoesDiariasVivoTravel, BigDecimal custoDiariasVivoTravel) {
		super();
		this.tempoLigacoesLocais = tempoLigacoesLocais;
		this.custoLigacoesLocais = custoLigacoesLocais;
		this.tempoCaixaPostal = tempoCaixaPostal;
		this.custoCaixaPostal = custoCaixaPostal;
		this.tempoLigacoesIntragrupo = tempoLigacoesIntragrupo;
		this.tempoLigacoesInterurbanas = tempoLigacoesInterurbanas;
		this.custoLigacoesInterurbanas = custoLigacoesInterurbanas;
		this.tempoLigacoesEmRoaming = tempoLigacoesEmRoaming;
		this.custoLigacoesEmRoaming = custoLigacoesEmRoaming;
		this.tempoLigacoesRecebidasACobrar = tempoLigacoesRecebidasACobrar;
		this.custoLigacoesRecebidasACobrar = custoLigacoesRecebidasACobrar;
		this.tempoLigacoesServicosTerceiros = tempoLigacoesServicosTerceiros;
		this.custoLigacoesServicosTerceiros = custoLigacoesServicosTerceiros;
		this.totalLojaDeServicosVivo = totalLojaDeServicosVivo;
		this.custoLojaDeServicosVivo = custoLojaDeServicosVivo;
		this.tempoLigacoesNoExterior = tempoLigacoesNoExterior;
		this.custoLigacoesNoExterior = custoLigacoesNoExterior;
		this.adicionalPorLigacoes = adicionalPorLigacoes;
		this.tempoLigacoesDiariasVivoTravel = tempoLigacoesDiariasVivoTravel;
		this.custoDiariasVivoTravel = custoDiariasVivoTravel;
		
		this.tempoLigacoes = somarTempoLigacoes(tempoLigacoesLocais, tempoCaixaPostal, tempoLigacoesIntragrupo, tempoLigacoesInterurbanas, tempoLigacoesEmRoaming,
				tempoLigacoesRecebidasACobrar, tempoLigacoesServicosTerceiros, tempoLigacoesNoExterior);
		this.custoLigacoes = this.custoLigacoes.add(custoLigacoesLocais).add(custoCaixaPostal).add(custoLigacoesInterurbanas).add(custoLigacoesEmRoaming)
				.add(custoLigacoesRecebidasACobrar).add(custoLigacoesServicosTerceiros).add(custoLojaDeServicosVivo).add(custoLigacoesNoExterior)
				.add(adicionalPorLigacoes).add(custoDiariasVivoTravel);
	}

	public String getTempoLigacoesPersonalizadas() {
		return tempoLigacoesPersonalizadas;
	}

	public void setTempoLigacoesPersonalizadas(String tempoLigacoesPersonalizadas) {
		this.tempoLigacoesPersonalizadas = tempoLigacoesPersonalizadas;
	}

	public String getTempoExcedenteLigacoesPersonalizadas() {
		return tempoExcedenteLigacoesPersonalizadas;
	}

	public void setTempoExcedenteLigacoesPersonalizadas(String tempoExcedenteLigacoesPersonalizadas) {
		this.tempoExcedenteLigacoesPersonalizadas = tempoExcedenteLigacoesPersonalizadas;
	}

	public BigDecimal getCustoLigacoesPersonalizadas() {
		return custoLigacoesPersonalizadas;
	}

	public void setCustoLigacoesPersonalizadas(BigDecimal custoLigacoesPersonalizadas) {
		this.custoLigacoesPersonalizadas = custoLigacoesPersonalizadas;
	}

	public BigDecimal getCustoLigacoesNaoIncluidas() {
		return custoLigacoesNaoIncluidas;
	}

	public void setCustoLigacoesNaoIncluidas(BigDecimal custoLigacoesNaoIncluidas) {
		this.custoLigacoesNaoIncluidas = custoLigacoesNaoIncluidas;
	}

	public String getTempoLigacoesLocais() {
		return tempoLigacoesLocais;
	}

	public BigDecimal getCustoLigacoesLocais() {
		return custoLigacoesLocais;
	}

	public String getTempoCaixaPostal() {
		return tempoCaixaPostal;
	}

	public BigDecimal getCustoCaixaPostal() {
		return custoCaixaPostal;
	}

	public String getTempoLigacoesIntragrupo() {
		return tempoLigacoesIntragrupo;
	}

	public String getTempoLigacoesInterurbanas() {
		return tempoLigacoesInterurbanas;
	}

	public BigDecimal getCustoLigacoesInterurbanas() {
		return custoLigacoesInterurbanas;
	}
	
	public String getTempoLigacoesEmRoaming() {
		return tempoLigacoesEmRoaming;
	}

	public BigDecimal getCustoLigacoesEmRoaming() {
		return custoLigacoesEmRoaming;
	}

	public String getTempoLigacoesRecebidasACobrar() {
		return tempoLigacoesRecebidasACobrar;
	}

	public BigDecimal getCustoLigacoesRecebidasACobrar() {
		return custoLigacoesRecebidasACobrar;
	}

	public String getTempoLigacoesServicosTerceiros() {
		return tempoLigacoesServicosTerceiros;
	}

	public BigDecimal getCustoLigacoesServicosTerceiros() {
		return custoLigacoesServicosTerceiros;
	}
	
	public int getTotalLojaDeServicosVivo() {
		return totalLojaDeServicosVivo;
	}

	public BigDecimal getCustoLojaDeServicosVivo() {
		return custoLojaDeServicosVivo;
	}

	public String getTempoLigacoesNoExterior() {
		return tempoLigacoesNoExterior;
	}

	public BigDecimal getCustoLigacoesNoExterior() {
		return custoLigacoesNoExterior;
	}
	
	public String getTempoLigacoes() {
		return tempoLigacoes;
	}

	public BigDecimal getCustoLigacoes() {
		return custoLigacoes;
	}
	
	public BigDecimal getAdicionalPorLigacoes() {
		return adicionalPorLigacoes;
	}
	
	public String getTempoLigacoesDiariasVivoTravel() {
		return tempoLigacoesDiariasVivoTravel;
	}

	public BigDecimal getCustoDiariasVivoTravel() {
		return custoDiariasVivoTravel;
	}

	private String somarTempoLigacoes(String... tempos) {
		String tempoLigacoes;
		int minutos = 0;
		int segundos = 0;
		
		for (String tempo : tempos) {
			tempo.replaceAll("\\.", "");
			String[] MinSeg = tempo.split("m");
			int min = Integer.parseInt(MinSeg[0]);
			int seg = Integer.parseInt((MinSeg[1].split("s"))[0]);
			minutos += min;
			segundos += seg;
		}
		
		if (segundos >= 60) {
			minutos += (int) segundos / 60;
			segundos = segundos % 60;
		}
		
		String aux = segundos + "";
		if (aux.matches("[0-9]"))
			tempoLigacoes = minutos + "m0" + segundos + "s";
		else
			tempoLigacoes = minutos + "m" + segundos + "s";
		
		return tempoLigacoes;
	}
}
