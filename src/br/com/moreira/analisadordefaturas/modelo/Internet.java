package br.com.moreira.analisadordefaturas.modelo;

import java.math.BigDecimal;
import java.util.List;

import br.com.moreira.analisadordefaturas.modelo.Mensagens.Planos;

public class Internet {
	private String totalUsoDeInternet = "0mb 0kb";
	private BigDecimal custoUsoDeIternet = BigDecimal.ZERO;
	private List<Planos> planos;
	
	public Internet() {
		
	}
	
	public Internet(String totalUsoDeInternet, BigDecimal custoUsoDeIternet,
			List<Planos> planos) {
		super();
		this.totalUsoDeInternet = totalUsoDeInternet;
		this.custoUsoDeIternet = custoUsoDeIternet;
		this.planos = planos;
	}
	
	public String getTotalUsoDeInternet() {
		return totalUsoDeInternet;
	}

	public BigDecimal getCustoUsoDeIternet() {
		return custoUsoDeIternet;
	}

	public List<Planos> getPlanos() {
		return planos;
	}
}