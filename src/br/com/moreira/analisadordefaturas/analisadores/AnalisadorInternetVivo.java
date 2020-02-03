package br.com.moreira.analisadordefaturas.analisadores;

import java.math.BigDecimal;

import br.com.moreira.analisadordefaturas.modelo.Internet;

public class AnalisadorInternetVivo {
	
	public Internet analisar(String[] fatura) {
		Internet internet = new Internet(
				gerarTempoUsoDeIternet(fatura),
				gerarCustoUsoDeIternet(fatura),
				null);
		return internet;
	}
	
	private String gerarTempoUsoDeIternet(String[] faturaArray) {
		int MB = 0;
		int KB= 0;

		int indice;
		for (int i = 0; i < faturaArray.length; i++) {
			if (faturaArray[i].equals("Internet") && faturaArray[i + 1].equals("-")) {
				indice = i;
				while (!faturaArray[indice].equals("Subtotal")) {
					indice++;
				}
				indice++;
				int mbParcial = Integer.parseInt(faturaArray[indice].replaceAll("[A-Z]{2}", ""));
				int kbParcial = Integer.parseInt(faturaArray[indice + 1].replaceAll("[A-Z]{2}", ""));
				MB += mbParcial;
				KB += kbParcial;
				i = indice;
			}
		}
		if (KB >= 1000) {
			MB += (int) MB / 1000;
			KB = KB % 1000;
		}
		return MB + "mb " + KB + "kb";
	}

	private BigDecimal gerarCustoUsoDeIternet(String[] faturaArray) {
		BigDecimal total = new BigDecimal("0.00");
		BigDecimal totalChecagem = new BigDecimal("0.00");

		for (int i = 0; i < faturaArray.length; i++) {
			if (faturaArray[i].equals("Internet") && faturaArray[i + 1].equals("-")) {
				while (!faturaArray[i].equals("Subtotal")) {
					totalChecagem = totalChecagem.add(checarTravelInternet(faturaArray[i], faturaArray[i + 1], faturaArray[i + 5]));
				    i++;
				}
				i = i + 3;
				String subtotal = faturaArray[i].replaceAll("\\.", "").replaceAll(",", "\\.");
			    total = total.add(new BigDecimal(subtotal));
			    
			    total = checarTotal(total, totalChecagem);
			}
		}
		return total;
	}

	private BigDecimal checarTotal(BigDecimal total, BigDecimal totalChecagem) {
		if(total.compareTo(BigDecimal.ZERO) == 0) {
			total = totalChecagem;
		} else if(total.compareTo(totalChecagem) != 0 && total.compareTo(BigDecimal.ZERO) > 0 && totalChecagem.compareTo(BigDecimal.ZERO) > 0) {
			total = total.add(totalChecagem);
		}
		return total;
	}

	private BigDecimal checarTravelInternet(String... params) {
		if(params[0].equals("TRAVEL") && params[1].equals("INTERNET"))
			return new BigDecimal(params[2].replaceAll("\\.", "").replaceAll(",", "\\."));
		return BigDecimal.ZERO;
	}

}
