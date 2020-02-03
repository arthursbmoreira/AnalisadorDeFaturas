package br.com.moreira.analisadordefaturas.servico;

import java.math.BigDecimal;
import java.util.List;

import br.com.moreira.analisadordefaturas.modelo.Empresa;
import br.com.moreira.analisadordefaturas.modelo.Fatura;

public class ConstrutorEmpresa {
	
	private final String PADRAO_MINUTO_SEGUNDO = "[0-9]*\\.*[0-9]+[m][0-9]{2}[s]";
	String PADRAO_DINHEIRO = "\\d{1,3}(\\.\\d{3})*(,\\d{2})";
	
	private List<Fatura> listaFaturas;
	private List<Fatura> listaFaturasNumerosNaoUtilizados;
	private String faturas;
	
	private int minutos;
	private int segundos;
	private String totalTempoLigacoes;
	private BigDecimal custoTotalFatura;
	private BigDecimal totalVerificacao;
	private BigDecimal custoTotalFaturaPersonalizada;
	private BigDecimal custoTotalAcimaDoContratado;
	private BigDecimal custoTotalPlanos;
	private BigDecimal descontos;
	private BigDecimal outrosLancamentos;
	private BigDecimal deducoesPorLei;
	
	public ConstrutorEmpresa(List<Fatura> listaFaturas, List<Fatura> listaFaturasNumerosNaoUtilizados, String faturas) {
		this.listaFaturas = listaFaturas;
		this.listaFaturasNumerosNaoUtilizados = listaFaturasNumerosNaoUtilizados;
		this.faturas = faturas;
	}
	
	public Empresa construir() {
		minutos = 0;
		segundos = 0;
		totalTempoLigacoes = "";
		custoTotalFatura = new BigDecimal("0.00");
		totalVerificacao = new BigDecimal("0.00");
		custoTotalFaturaPersonalizada = new BigDecimal("0.00");
		custoTotalAcimaDoContratado = new BigDecimal("0.00");
		custoTotalPlanos = new BigDecimal("0.00");
		descontos = new BigDecimal("0.00");
		outrosLancamentos = new BigDecimal("0.00");
		deducoesPorLei = new BigDecimal("0.00"); 
		
		
		for (Fatura fatura : listaFaturas) {
			gerarTotalTempoLigacoes(fatura);
			gerarCustoTotalFatura(fatura);
			gerarCustoTotalFaturaPersonalizada(fatura);
			gerarCustoTotalAcimaDoContratado(fatura);
			gerarCustoTotalPlanos(fatura);
			gerarDescontos(fatura);
		}
		
		for (Fatura fatura : listaFaturasNumerosNaoUtilizados) {
			gerarTotalTempoLigacoes(fatura);
			gerarCustoTotalFatura(fatura);
			gerarCustoTotalAcimaDoContratado(fatura);
			gerarCustoTotalPlanos(fatura);
			gerarDescontos(fatura);
		}
		
		String dadosFatura[] = gerarDadosFatura(faturas).split("\\s+");
		gerarTotalVerificacao(dadosFatura);
		gerarOutrosLancamentos(dadosFatura);
		gerarDeducoesPorLei(dadosFatura);
		
		totalTempoLigacoes = montarRetornoMinSeg(this.minutos, this.segundos);
		
		return new Empresa(totalTempoLigacoes, custoTotalFatura,
				custoTotalFaturaPersonalizada, custoTotalAcimaDoContratado,
				custoTotalPlanos, descontos, totalVerificacao,
				outrosLancamentos, deducoesPorLei, listaFaturas.size(),
				listaFaturasNumerosNaoUtilizados.size());
	}
	
	private void gerarTotalTempoLigacoes(Fatura fatura) {
		String[] tempo;
		
		tempo = fatura.getLigacoes().getTempoCaixaPostal().split("m");
		minutos += Integer.parseInt(tempo[0]);
		segundos += Integer.parseInt(tempo[1].split("s")[0]);
		tempo = fatura.getLigacoes().getTempoLigacoesEmRoaming().split("m");
		minutos += Integer.parseInt(tempo[0]);
		segundos += Integer.parseInt(tempo[1].split("s")[0]);
		tempo = fatura.getLigacoes().getTempoLigacoesInterurbanas().split("m");
		minutos += Integer.parseInt(tempo[0]);
		segundos += Integer.parseInt(tempo[1].split("s")[0]);
		tempo = fatura.getLigacoes().getTempoLigacoesIntragrupo().split("m");
		minutos += Integer.parseInt(tempo[0]);
		segundos += Integer.parseInt(tempo[1].split("s")[0]);
		tempo = fatura.getLigacoes().getTempoLigacoesLocais().split("m");
		minutos += Integer.parseInt(tempo[0]);
		segundos += Integer.parseInt(tempo[1].split("s")[0]);
		tempo = fatura.getLigacoes().getTempoLigacoesRecebidasACobrar().split("m");
		minutos += Integer.parseInt(tempo[0]);
		segundos += Integer.parseInt(tempo[1].split("s")[0]);
		tempo = fatura.getLigacoes().getTempoLigacoesServicosTerceiros().split("m");
		minutos += Integer.parseInt(tempo[0]);
		segundos += Integer.parseInt(tempo[1].split("s")[0]);
	}
	
	private void gerarCustoTotalFatura(Fatura fatura) {
		custoTotalFatura = custoTotalFatura.add(fatura.getCustoTotalFatura());
	}
	
	private void gerarCustoTotalFaturaPersonalizada(Fatura fatura) {
		custoTotalFaturaPersonalizada = custoTotalFaturaPersonalizada.add(fatura.getCustoTotalFaturaPersonalizada());
	}
	
	private void gerarCustoTotalAcimaDoContratado(Fatura fatura) {
		custoTotalAcimaDoContratado = custoTotalAcimaDoContratado.add(fatura.getCustoTotalFatura().subtract((fatura.getCustoServicosContratados())));
	}
	
	private void gerarCustoTotalPlanos(Fatura fatura) {
		custoTotalPlanos = custoTotalPlanos.add(fatura.getCustoServicosContratados());
	}

	private void gerarDescontos(Fatura fatura) {
		descontos = descontos.add(fatura.getDescontos());
	}
	
	private String gerarDadosFatura(String texto) {
        int fim = texto.indexOf("NOTA FISCAL DE SERVIÇOS DE TELECOMUNICAÇÕES");
        String trecho = texto.substring(0, fim);
        return trecho;
    }
	
	private void gerarTotalVerificacao(String[] dadosFatura) {
		for (int i = 0; i < dadosFatura.length; i++) {
			if(dadosFatura[i].equals("Total")) {
				if(dadosFatura[i + 1].equals("a") && dadosFatura[i + 2].equals("Pagar") && dadosFatura[i + 4].equals("R$")) {
					String total = dadosFatura[i + 6].replaceAll("\\.", "").replaceAll(",", "\\.");
					totalVerificacao = totalVerificacao.add(new BigDecimal(total));
					break;
				}
			}
		}
		
	}
	
	private void gerarOutrosLancamentos(String[] dadosFatura) {
		for (int i = 0; i < dadosFatura.length; i++) {
			if(dadosFatura[i].equals("Outros")) {
				if(dadosFatura[i + 1].equals("Lançamentos")) {
					while(!dadosFatura[i].equals("Subtotal")) {
						i++;
					}
					i++;
					String subtotal = dadosFatura[i].replaceAll("\\.", "").replaceAll(",", "\\.").replaceAll("-", "");
					outrosLancamentos = outrosLancamentos.add(new BigDecimal(subtotal));
					outrosLancamentos = descontos.subtract(outrosLancamentos);
				}
			}
		}
	}
	
	private void gerarDeducoesPorLei(String[] dadosFatura) {
		String subtotal = "";
		for (int i = 0; i < dadosFatura.length; i++) {
			if(dadosFatura[i].equals("Dedução") && dadosFatura[i + 1].equals("relativa") && dadosFatura[i + 3].equals("Lei")) {
				i = i + 8;
				if(dadosFatura[i].replaceAll("-", "").matches(PADRAO_DINHEIRO)) {
					subtotal = dadosFatura[i].replaceAll("\\.", "").replaceAll(",", "\\.").replaceAll("-", "");
					deducoesPorLei = deducoesPorLei.add(new BigDecimal(subtotal));
				}
			}
		}
	}
	
	protected int retornarMinSeg(String[] faturaArray, int indice, int i) {		
		while (!faturaArray[indice].equals("Subtotal")) {
			indice++;
		}
		indice++;
		if (faturaArray[indice].matches(PADRAO_MINUTO_SEGUNDO)) {
			String tempo = faturaArray[indice].replaceAll("\\.", "");
			String[] MinSeg = tempo.split("m");
			int min = Integer.parseInt(MinSeg[0]);
			int seg = Integer.parseInt((MinSeg[1].split("s"))[0]);
			minutos += min;
			segundos += seg;
			i = indice;
		}
		return i;
	}

	protected String montarRetornoMinSeg(int minutos, int segundos) {
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
	
}
