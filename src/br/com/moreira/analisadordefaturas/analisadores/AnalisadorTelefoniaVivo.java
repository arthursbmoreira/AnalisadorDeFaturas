package br.com.moreira.analisadordefaturas.analisadores;

import java.math.BigDecimal;

import br.com.moreira.analisadordefaturas.modelo.Ligacoes;

public class AnalisadorTelefoniaVivo {

	private static final String PADRAO_MINUTO_SEGUNDO = "[0-9]*\\.*[0-9]+[m][0-9]{2}[s]";
	private static final String PADRAO_DINHEIRO = "\\d{1,3}(\\.\\d{3})*(,\\d{2})";

	private int minutosLocais;
	private int segundosLocais;
	private BigDecimal totalLocal;

	private int minutosCaixaPostal;
	private int segundosCaixaPostal;
	private BigDecimal totalCaixaPostal;
	
	private int minutosIntragrupo;
	private int segundosIntragrupo;
	
	private int minutosInterurbanos;
	private int segundosInterurbanos;
	private BigDecimal totalInterurbanos;
	
	private int minutosEmRoaming;
	private int segundosEmRoaming;
	private BigDecimal totalEmRoaming;
	
	private int minutosACobar;
	private int segundosACobar;
	private BigDecimal totalACobar;
	
	private int minutosTerceiros;
	private int segundosTerceiros;
	private BigDecimal totalTerceiros;
	
	private int quantidadeLojaVivo;
	private BigDecimal totalLojaVivo;
	
	private int minutosExterior;
	private int segundosExterior;
	private BigDecimal totalExterior;
	
	private BigDecimal adicionalPorLigacoes;
	
	private int minutosVivoTravel;
	private int segundosVivoTravel;
	private BigDecimal totalVivoTravel;

	public Ligacoes analisar(String[] fatura) {
		limparVariaveis();

		for (int i = 0; i < fatura.length; i++) {
			i = analisarLigacoesLocais(fatura, i);

			i = analisarCaixaPostal(fatura, i);
			
			i = analisarIntragrupo(fatura, i);
			
			i = analisarLigacoesInterurbanas(fatura, i);
			
			i = analisarLigacoesEmRoaming(fatura, i);

			i = analisarLigacoesRecebidasACobrar(fatura, i);
			
			i = analisarLigacoesServicosDeTerceiros(fatura, i);
			
			i = gerarTotalLojaDeServicosVivo(fatura, i);
			
			i = analisarLigacoesNoExterior(fatura, i);
			
			i = gerarAdicionalPorLigacoes(fatura, i);
			
			i = analisarDiariasVivoTravel(fatura, i);
			
		}
		String tempoLigacoesLocais = montarRetornoMinSeg(this.minutosLocais, this.segundosLocais);
		String tempoCaixaPostal = montarRetornoMinSeg(this.minutosCaixaPostal, this.segundosCaixaPostal);
		String tempoIntragrupo = montarRetornoMinSeg(this.minutosIntragrupo, this.segundosIntragrupo);
		String tempoLigacoesInterurbanas = montarRetornoMinSeg(this.minutosInterurbanos, this.segundosInterurbanos);
		String tempoLigacoesEmRoaming = montarRetornoMinSeg(this.minutosEmRoaming, this.segundosEmRoaming);
		String tempoLigacoesRecebidasACobrar = montarRetornoMinSeg(this.minutosACobar, this.segundosACobar);
		String tempoLigacoesServicosDeTerceiros = montarRetornoMinSeg(this.minutosTerceiros, this.segundosTerceiros);
		String tempoLigacoesNoExterior = montarRetornoMinSeg(this.minutosExterior, this.segundosExterior);
		String tempoDiariasVivoTravel = montarRetornoMinSeg(this.minutosVivoTravel, this.segundosVivoTravel);

		Ligacoes ligacoes = new Ligacoes(tempoLigacoesLocais, // tempoLigacoesLocais
				totalLocal, // custoLigacoesLocais
				tempoCaixaPostal, // tempoCaixaPostal
				totalCaixaPostal, // custoCaixaPostal
				tempoIntragrupo, // tempoLigacoesIntragrupo
				tempoLigacoesInterurbanas, // tempoLigacoesInterurbanas
				totalInterurbanos, // custoLigacoesInterurbanas
				tempoLigacoesEmRoaming, // tempoLigacoesEmRoaming
				totalEmRoaming, // custoLigacoesEmRoaming
				tempoLigacoesRecebidasACobrar, // tempoLigacoesRecebidasACobrar
				totalACobar, // custoLigacoesRecebidasACobrar
				tempoLigacoesServicosDeTerceiros, // tempoLigacoesServicosTerceiros
				totalTerceiros, // custoLigacoesServicosTerceiros
				quantidadeLojaVivo, // quantidadeLojaDeServicosVivo
				totalLojaVivo, // custoLojaDeServicosVivo
				tempoLigacoesNoExterior, // tempoLigacoesNoExterior
				totalExterior, // custoLigacoesNoExterior
				adicionalPorLigacoes, // adicionalPorLigacoes
				tempoDiariasVivoTravel,
				totalVivoTravel); // custoDiariasVivoTravel

		return ligacoes;
	}

	private void limparVariaveis() {
		minutosLocais = 0;
		segundosLocais = 0;
		totalLocal = new BigDecimal("0.00");

		minutosCaixaPostal = 0;
		segundosCaixaPostal = 0;
		totalCaixaPostal = new BigDecimal("0.00");
		
		minutosIntragrupo = 0;
		segundosIntragrupo = 0;
		
		minutosInterurbanos = 0;
		segundosInterurbanos = 0;
		totalInterurbanos = new BigDecimal("0.00");
		
		minutosEmRoaming = 0;
		segundosEmRoaming = 0;
		totalEmRoaming = new BigDecimal("0.00");
		
		minutosACobar = 0;
		segundosACobar = 0;
		totalACobar = new BigDecimal("0.00");
		
		minutosTerceiros = 0;
		segundosTerceiros = 0;
		totalTerceiros = new BigDecimal("0.00");
		
		totalLojaVivo = new BigDecimal("0.00");
		quantidadeLojaVivo = 0;
		
		minutosExterior = 0;
		segundosExterior = 0;
		totalExterior = new BigDecimal("0.00");
		
		adicionalPorLigacoes = new BigDecimal("0.00");
		
		minutosVivoTravel = 0;
		segundosVivoTravel = 0;
		totalVivoTravel = new BigDecimal("0.00");
	}
	
	private int analisarLigacoesLocais(String[] faturaArray, int indice) {
		if (faturaArray[indice].equals("Ligações") && faturaArray[indice + 1].equals("Locais") && !faturaArray[indice + 4].equals("Grupo") && !faturaArray[indice + 4].equals("Cobrar")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String tempo = faturaArray[indice];
			this.minutosLocais += retornarMin(tempo);
			this.segundosLocais += retornarSeg(tempo);
			String subtotal = faturaArray[++indice];
			this.totalLocal = this.totalLocal.add(retornarTotal(subtotal));
		}
		return indice;
	}

	private int analisarCaixaPostal(String[] faturaArray, int indice) {
		if (faturaArray[indice].equals("Acesso") && faturaArray[indice + 1].equals("a") && faturaArray[indice + 2].equals("Caixa") && faturaArray[indice + 3].equals("Postal")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String tempo = faturaArray[indice];
			minutosCaixaPostal += retornarMin(tempo);
			segundosCaixaPostal += retornarSeg(tempo);
			String subtotal = faturaArray[++indice];
			this.totalCaixaPostal = this.totalCaixaPostal.add(retornarTotal(subtotal));
		}
		return indice;
	}

	private int analisarIntragrupo(String[] faturaArray, int indice) {
		if (faturaArray[indice].equals("Ligações") && faturaArray[indice + 1].equals("Locais") && faturaArray[indice + 3].equals("Para") && faturaArray[indice + 4].equals("Grupo")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String tempo = faturaArray[indice];
			minutosIntragrupo += retornarMin(tempo);
			segundosIntragrupo += retornarSeg(tempo);
			indice++;
		}
		return indice;
	}

	private int analisarLigacoesInterurbanas(String[] faturaArray, int indice) {
		// Verificando ligações para dentro do estado
		if (faturaArray[indice].equals("Para") && faturaArray[indice + 1].equals("Dentro") && faturaArray[indice + 3].equals("Estado")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String tempo = faturaArray[indice];
			minutosInterurbanos += retornarMin(tempo);
			segundosInterurbanos += retornarSeg(tempo);
			String subtotal = faturaArray[++indice];
			totalInterurbanos = totalInterurbanos.add(retornarTotal(subtotal));
		}
		// Verificando ligações para fora do estado
		if (faturaArray[indice].equals("Para") && faturaArray[indice + 1].equals("Outros") && faturaArray[indice + 2].equals("Estados")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String tempo = faturaArray[indice];
			minutosInterurbanos += retornarMin(tempo);
			segundosInterurbanos += retornarSeg(tempo);
			String subtotal = faturaArray[++indice];
			totalInterurbanos = totalInterurbanos.add(retornarTotal(subtotal));
		}
		// Verificando ligações recebidas a cobrar de outras localidades
		if (faturaArray[indice].equals("Recebidas") && faturaArray[indice + 2].equals("Cobrar")
				&& faturaArray[indice + 4].equals("Outra") && faturaArray[indice + 5].equals("Localidade")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String tempo = faturaArray[indice];
			minutosInterurbanos += retornarMin(tempo);
			segundosInterurbanos += retornarSeg(tempo);
			String subtotal = faturaArray[++indice];
			totalInterurbanos = totalInterurbanos.add(retornarTotal(subtotal));
		}
		// Ligações de longa distância feitas em roaming
		if (faturaArray[indice].equals("Para") && faturaArray[indice + 1].equals("Fixo")) {
			if ((!faturaArray[indice - 3].equals("Ligações") && !faturaArray[indice - 2].equals("Locais"))
					&& (!faturaArray[indice - 4].equals("Dentro") && !faturaArray[indice - 2].equals("Estado"))
					&& (!faturaArray[indice - 3].equals("Outros") && !faturaArray[indice - 2].equals("Estados"))) {
				indice = retornarIndiceSubtotal(faturaArray, indice);
				String tempo = faturaArray[indice];
				minutosInterurbanos += retornarMin(tempo);
				segundosInterurbanos += retornarSeg(tempo);
				String subtotal = faturaArray[++indice];
				totalInterurbanos = totalInterurbanos.add(retornarTotal(subtotal));
			}
		}
		if (faturaArray[indice].equals("Para") && faturaArray[indice + 1].equals("Celulares")) {
			if ((!faturaArray[indice - 3].equals("Ligações") && !faturaArray[indice - 2].equals("Locais"))
					&& (!faturaArray[indice - 4].equals("Dentro") && !faturaArray[indice - 2].equals("Estado"))
					&& (!faturaArray[indice - 3].equals("Outros") && !faturaArray[indice - 2].equals("Estados"))) {
				indice = retornarIndiceSubtotal(faturaArray, indice);
				String tempo = faturaArray[indice];
				minutosInterurbanos += retornarMin(tempo);
				segundosInterurbanos += retornarSeg(tempo);
				String subtotal = faturaArray[++indice];
				totalInterurbanos = totalInterurbanos.add(retornarTotal(subtotal));
			}
		}
		return indice;
	}

	private int analisarLigacoesEmRoaming(String[] faturaArray, int indice) {
		if (faturaArray[indice].equals("Ligações") && faturaArray[indice + 1].equals("Recebidas") && faturaArray[indice + 2].equals("em") && faturaArray[indice + 3].equals("Roaming")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String tempo = faturaArray[indice];
			minutosEmRoaming += retornarMin(tempo);
			segundosEmRoaming += retornarSeg(tempo);
			String subtotal = faturaArray[++indice];
			totalEmRoaming = totalEmRoaming.add(retornarTotal(subtotal));
		}
		return indice;
	}

	private int analisarLigacoesRecebidasACobrar(String[] faturaArray, int indice) {
		if (faturaArray[indice].equals("Ligações") && faturaArray[indice + 1].equals("Locais") && faturaArray[indice + 2].equals("Recebidas") && faturaArray[indice + 4].equals("Cobrar")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String tempo = faturaArray[indice];
			minutosACobar += retornarMin(tempo);
			segundosACobar += retornarSeg(tempo);
			String subtotal = faturaArray[++indice];
			totalACobar = totalACobar.add(retornarTotal(subtotal));
		}
		return indice;
	}

	private int analisarLigacoesServicosDeTerceiros(String[] faturaArray, int indice) {
		if (faturaArray[indice].equals("Ligações") && faturaArray[indice + 2].equals("Serviços") && faturaArray[indice + 4].equals("Terceiros")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String tempo = faturaArray[indice];
			minutosTerceiros += retornarMin(tempo);
			segundosTerceiros += retornarSeg(tempo);
			String subtotal = faturaArray[++indice];
			totalTerceiros = totalTerceiros.add(retornarTotal(subtotal));
		}
		return indice;
	}

	private int gerarTotalLojaDeServicosVivo(String[] faturaArray, int indice) {
		if (faturaArray[indice].equals("Loja") && faturaArray[indice + 2].equals("Serviços") && faturaArray[indice + 3].equals("Vivo")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String quantidade = faturaArray[indice];
			quantidadeLojaVivo += Integer.parseInt(quantidade);
			String subtotal = faturaArray[++indice];
			totalLojaVivo = totalLojaVivo.add(retornarTotal(subtotal));
		}
		return indice;
	}

	private int analisarLigacoesNoExterior(String[] faturaArray, int indice) {
		if (faturaArray[indice].equals("NO") && faturaArray[indice + 1].equals("EXTERIOR")) {
			if (faturaArray[indice + 2].equals("Ligações") && faturaArray[indice + 3].equals("Realizadas") && faturaArray[indice + 5].equals("Recebidas")) {
				indice = retornarIndiceSubtotal(faturaArray, indice);
				String tempo = faturaArray[indice];
				minutosExterior += retornarMin(tempo);
				segundosExterior += retornarSeg(tempo);
				String subtotal = faturaArray[++indice];
				totalExterior = totalExterior.add(retornarTotal(subtotal));
			}
		}
		if (faturaArray[indice].equals("Para") && faturaArray[indice + 1].equals("Outros") && (faturaArray[indice + 2].equals("Países") || faturaArray[indice + 2].equals("Paises"))) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			minutosExterior += retornarMin(faturaArray[indice]);
			segundosExterior += retornarSeg(faturaArray[indice]);
			String subtotal = faturaArray[++indice];
			totalExterior = totalExterior.add(retornarTotal(subtotal));
		}
		return indice;
	}

	private int gerarAdicionalPorLigacoes(String[] faturaArray, int indice) {
		if (faturaArray[indice].equals("Adicional") && faturaArray[indice + 1].equals("por") && faturaArray[indice + 2].equals("Ligações")) {
			indice = retornarIndiceSubtotal(faturaArray, indice);
			String subtotal = faturaArray[++indice];
			adicionalPorLigacoes = adicionalPorLigacoes.add(retornarTotal(subtotal));
		}
		return indice;
	}

	private int analisarDiariasVivoTravel(String[] faturaArray, int indice) {
		if (faturaArray[indice].equals("Diaria") || faturaArray[indice].equals("Diarias") || faturaArray[indice].equals("Diária") || faturaArray[indice].equals("Diárias")) {
			if (faturaArray[indice + 1].equals("Vivo") && faturaArray[indice + 2].equals("Travel")) {
				indice = retornarIndiceSubtotal(faturaArray, indice);
				String tempo = faturaArray[indice];
				minutosVivoTravel += retornarMin(tempo);
				segundosVivoTravel += retornarSeg(tempo);
				String subtotal = faturaArray[++indice];
				totalVivoTravel = totalVivoTravel.add(retornarTotal(subtotal));
			}
		}
		return indice;
	}

	/**
	 * @param faturaArray
	 * @param indice
	 * @return Retorna o índice do subtotal +1
	 */
	private int retornarIndiceSubtotal(String[] faturaArray, int indice) {
		while (!faturaArray[indice].equals("Subtotal")) {
			indice++;
		}
		return ++indice;
	}

	private int retornarMin(String tempo) {
		if (tempo.matches(PADRAO_MINUTO_SEGUNDO))
			return Integer.parseInt(tempo.replaceAll("\\.", "").split("m")[0]);
		return 0;
	}

	private int retornarSeg(String tempo) {
		if (tempo.matches(PADRAO_MINUTO_SEGUNDO))
			return Integer.parseInt((tempo.replaceAll("\\.", "").split("m")[1]).split("s")[0]);
		return 0;
	}

	private String montarRetornoMinSeg(int minutos, int segundos) {
		if (segundos >= 60) {
			minutos += (int) segundos / 60;
			segundos = segundos % 60;
		}
		String aux = segundos + "";
		if (aux.matches("[0-9]"))
			return minutos + "m0" + segundos + "s";
		else
			return minutos + "m" + segundos + "s";
	}

	private BigDecimal retornarTotal(String valor) {
		if (valor.matches(PADRAO_DINHEIRO))
			return new BigDecimal(valor.replaceAll("\\.", "").replaceAll(",", "\\."));
		return BigDecimal.ZERO;
	}
}