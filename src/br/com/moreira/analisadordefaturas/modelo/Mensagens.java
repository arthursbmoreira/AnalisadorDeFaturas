package br.com.moreira.analisadordefaturas.modelo;

import java.math.BigDecimal;
import java.util.List;

public class Mensagens {
	private int totalMensagensNormaisEnviadas = 0;
	private BigDecimal custoMensagensNormaisEnviadas = BigDecimal.ZERO;
	private BigDecimal adicionalPorMensagensEnviadas = BigDecimal.ZERO;
	private int totalMensagensPlanoEnviadas = 0;
	private int totalFotosTorpedoEnviados = 0;
	private BigDecimal custoFotoTorpedosEnviados = BigDecimal.ZERO;
	private int totalMensagensOutrosServicosEnviadas = 0;
	private BigDecimal custoMensagensOutrosServicosEnviadas = BigDecimal.ZERO;
	private int totalMensagens = 0;
	private BigDecimal custoMensagens = BigDecimal.ZERO;

	private List<Planos> planos;

	public enum Planos {
		PLANO1, PLANO2, PLANO3;
	}

	public Mensagens() {
		
	}
	
	public Mensagens(int totalMensagensNormaisEnviadas, BigDecimal custoMensagensNormaisEnviadas,
			BigDecimal adicionalPorMensagensEnviadas, int totalMensagensPlano,
			int totalFotosTorpedoEnviados,
			BigDecimal custoFotoTorpedosEnviados,
			int totalMensagensOutrosServicosEnviadas,
			BigDecimal custoMensagensOutrosServicosEnviadas, List<Planos> planos) {
		super();
		this.totalMensagensNormaisEnviadas = totalMensagensNormaisEnviadas;
		this.custoMensagensNormaisEnviadas = custoMensagensNormaisEnviadas;
		this.adicionalPorMensagensEnviadas = adicionalPorMensagensEnviadas;
		this.totalMensagensPlanoEnviadas = totalMensagensPlano;
		this.totalFotosTorpedoEnviados = totalFotosTorpedoEnviados;
		this.custoFotoTorpedosEnviados = custoFotoTorpedosEnviados;
		this.totalMensagensOutrosServicosEnviadas = totalMensagensOutrosServicosEnviadas;
		this.custoMensagensOutrosServicosEnviadas = custoMensagensOutrosServicosEnviadas;
		this.totalMensagens = totalMensagensNormaisEnviadas + totalMensagensPlano + totalFotosTorpedoEnviados + totalMensagensOutrosServicosEnviadas;
		this.custoMensagens = this.custoMensagens.add(custoMensagensNormaisEnviadas)
												 .add(adicionalPorMensagensEnviadas)
												 .add(custoFotoTorpedosEnviados)
												 .add(custoMensagensOutrosServicosEnviadas);
		this.planos = planos;
	}
	
	public int getTotalMensagensNormaisEnviadas() {
		return totalMensagensNormaisEnviadas;
	}
	
	public BigDecimal getCustoMensagensNormaisEnviadas() {
		return custoMensagensNormaisEnviadas;
	}
	
	public BigDecimal getAdicionalPorMensagensEnviadas() {
		return adicionalPorMensagensEnviadas;
	}

	public int getTotalMensagensPlanoEnviadas() {
		return totalMensagensPlanoEnviadas;
	}

	public int getTotalFotosTorpedoEnviados() {
		return totalFotosTorpedoEnviados;
	}

	public BigDecimal getCustoFotoTorpedosEnviados() {
		return custoFotoTorpedosEnviados;
	}

	public int getTotalMensagensOutrosServicosEnviadas() {
		return totalMensagensOutrosServicosEnviadas;
	}

	public BigDecimal getCustoMensagensOutrosServicosEnviadas() {
		return custoMensagensOutrosServicosEnviadas;
	}
	
	public BigDecimal getCustoMensagens() {
		return custoMensagens;
	}

	public int getTotalMensagens() {
		return totalMensagens;
	}

	public List<Planos> getPlanos() {
		return planos;
	}
}
