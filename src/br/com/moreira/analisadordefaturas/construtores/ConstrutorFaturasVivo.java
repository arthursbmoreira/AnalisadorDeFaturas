package br.com.moreira.analisadordefaturas.construtores;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.moreira.analisadordefaturas.analisadores.AnalisadorInternetVivo;
import br.com.moreira.analisadordefaturas.analisadores.AnalisadorMensagensVivo;
import br.com.moreira.analisadordefaturas.analisadores.AnalisadorTelefoniaVivo;
import br.com.moreira.analisadordefaturas.dao.LinhaDAO;
import br.com.moreira.analisadordefaturas.exceptions.ArquivoInvalidoException;
import br.com.moreira.analisadordefaturas.exceptions.PlanoException;
import br.com.moreira.analisadordefaturas.filtro.ConfiguracoesPersonalizadas;
import br.com.moreira.analisadordefaturas.modelo.Cliente;
import br.com.moreira.analisadordefaturas.modelo.Fatura;
import br.com.moreira.analisadordefaturas.modelo.Linha;
import br.com.moreira.analisadordefaturas.modelo.PlanoSubstituicao;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

public class ConstrutorFaturasVivo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String faturas;
	private Cliente cliente;
	private ConfiguracoesPersonalizadas config;
	
	private AnalisadorTelefoniaVivo analisadorLigacoes;
	private AnalisadorMensagensVivo analisadorMensagens;
	private AnalisadorInternetVivo analisadorInternet;
	
	private List<Fatura> listaFaturas = new ArrayList<>();
	private List<Fatura> listaFaturasNumerosNaoUtilizados = new ArrayList<>();
	private List<Fatura> listaFaturasComErro = new ArrayList<>();
	
	public ConstrutorFaturasVivo(String faturas, Cliente cliente, ConfiguracoesPersonalizadas config,
			AnalisadorTelefoniaVivo analisadorLigacoes, AnalisadorMensagensVivo analisadorMensagens, AnalisadorInternetVivo analisadorInternet) {
		this.faturas = faturas;
		this.cliente = cliente;
		this.config = config;
		this.analisadorLigacoes = analisadorLigacoes;
		this.analisadorMensagens = analisadorMensagens;
		this.analisadorInternet = analisadorInternet;
	}
	
	public void construirFaturas() {
		try {
			List<String> listaFaturasIndividuais = prepararFaturasParaAnalise();
			String dadosFatura[] = gerarDadosFatura();
			//verificarPlanoCliente(listaFaturasIndividuais);
			List<Linha> linhas = listarLinhas(cliente.getId());
			verificarNovasLinhas(listaFaturasIndividuais, linhas);
			List<PlanoSubstituicao> planosSubstituicao = cliente.getPlanosSubstituicao();
			
			String arrayFatura[];
			String numero;
	        for (String fatura : listaFaturasIndividuais) {
	            arrayFatura = fatura.split("\\s+");
	            
	            numero = getNumero(arrayFatura);
	            
	            Linha linha = associarLinhaCliente(numero, linhas);
	            
	    		Fatura f = new Fatura(
				linha,
				analisadorLigacoes.analisar(arrayFatura),
				analisadorMensagens.analisar(arrayFatura),
				analisadorInternet.analisar(arrayFatura),
				gerarValorSubstituicao(fatura, planosSubstituicao),
				gerarCustoServicosContratados(arrayFatura),
				gerarTotalVerificacao(arrayFatura),
				gerarDescontos(dadosFatura, numero));
				
	    		if(!f.isTotalCorreto()) {
	    			listaFaturasComErro.add(f);
	    		}
	    		
	    		if(isFaturaUtilizada(f))
	    			listaFaturas.add(f);
	    		else
	    			listaFaturasNumerosNaoUtilizados.add(f);
	        }
	        
	        if(config.isAtivo()) {
	        	listaFaturas = config.aplicarConfiguracoesPersonalizadas(listaFaturas);
	        }
		} catch(StringIndexOutOfBoundsException e) {
			throw new ArquivoInvalidoException();
		}
	}
	
	private BigDecimal gerarValorSubstituicao(String fatura, List<PlanoSubstituicao> planosSubstituicao) {
		BigDecimal total = new BigDecimal("0.00");
		for (PlanoSubstituicao plano : planosSubstituicao) {
			if(fatura.contains(plano.getNome())) {
				total = total.add(plano.getValor());
			}
		}
		return total;
	}
	
	private List<String> prepararFaturasParaAnalise() throws StringIndexOutOfBoundsException {
		List<String> listaDePaginas = gerarListaDePaginas();
		List<String> listaFaturasIndividuais = gerarListaDeFaturasIndividuais(listaDePaginas);
		
		return listaFaturasIndividuais;
	}

	private String[] gerarDadosFatura(){
		int	inicio = faturas.indexOf("VEJA O USO DETALHADO DO VIVO");
		String[] dadosFatura = faturas.substring(0, inicio).split("\\s+");
		
		return dadosFatura;
    }
	
	private List<String> gerarListaDePaginas() throws StringIndexOutOfBoundsException {
		String faturas = this.faturas;
        int inicio = faturas.indexOf("VEJA O USO DETALHADO DO VIVO");
        if(inicio == -1) {
        	throw new StringIndexOutOfBoundsException();
        }
        
        List<String> paginasFatura = new ArrayList<>();
        while (inicio != -1) {
            paginasFatura.add(faturas.substring(0, inicio));
            faturas = faturas.substring(inicio + 1);
            inicio = faturas.indexOf("VEJA O USO DETALHADO DO VIVO");
        }
        paginasFatura.add(faturas);
        paginasFatura.remove(0);
        
        return paginasFatura;
    }
	
	private List<String> gerarListaDeFaturasIndividuais(List<String> paginas) {
		List<String> listaFaturasIndividuais = new ArrayList<>();
        StringBuilder concatenadorFatura = new StringBuilder(paginas.get(0));
        paginas.remove(0);
        for (String s : paginas) {
            String numeroCelular = s.substring(28, 41);//posição onde se encontra o número da linha no texto
            String numeroFixo = s.substring(28, 38);
            if (numeroCelular.trim().matches("[0-9]{2}\\-[0-9]+\\-[0-9]+")) {
                if (!numeroCelular.equals(concatenadorFatura.substring(28, 41))) {
                    listaFaturasIndividuais.add(concatenadorFatura.toString());
                    concatenadorFatura = new StringBuilder(s);
                } else {
                    concatenadorFatura.append(s);
                }
            }
            if (numeroFixo.trim().matches("[0-9]+")) {
                if (!numeroFixo.equals(concatenadorFatura.substring(28, 38))) {
                    listaFaturasIndividuais.add(concatenadorFatura.toString());
                    concatenadorFatura = new StringBuilder(s);
                } else {
                    concatenadorFatura.append(s);
                }
            }
        }
        listaFaturasIndividuais.add(concatenadorFatura.toString());
        
        return listaFaturasIndividuais;
    }
	
	private BigDecimal gerarDescontos(String[] dadosFatura, String numero) {
		BigDecimal desconto = new BigDecimal("0.00");
		
		int indice;
		for (int i = 0; i < dadosFatura.length; i++) {
			if(dadosFatura[i].equals("DESCONTOS") && dadosFatura[i + 1].equals("E") && dadosFatura[i + 2].equals("PROMOÇÕES")) {
				indice = i;
				while(!dadosFatura[indice].equals("Subtotal")) {
					if(dadosFatura[indice].equals(numero)) {
						while(!dadosFatura[indice].matches("-\\d{1,3}(\\.\\d{3})*(,\\d{2})")) {
							indice++;
						}
						String subtotal = dadosFatura[indice].replaceAll("\\.", "").replaceAll(",", "\\.").replaceAll("-", "");
					    desconto = desconto.add(new BigDecimal(subtotal));
					}
					indice++;
				}
				i = indice;
			}
		}
		return desconto;
	}

	private String getNumero(String[] arrayFatura) {
        String numero = arrayFatura[6];
        if (numero.matches("[0-9]{2}\\-[0-9]+\\-[0-9]+"))
            return numero;
        if (numero.matches("[0-9]+"))
            return numero;
        return null;
    }

	private BigDecimal gerarCustoServicosContratados(String[] arrayFatura) {
		BigDecimal total = new BigDecimal("0.00");
		
		for (int i = 0; i < arrayFatura.length; i++) {
			if (arrayFatura[i].equals("VALOR") && arrayFatura[i + 1].equals("DO") && arrayFatura[i + 2].equals("VIVO")) {
				while (!arrayFatura[i].equals("SERVIÇOS") && !arrayFatura[i + 1].equals("CONTRATADOS")) {
				    if(arrayFatura[i].equals("TOTAL"))
				    	return total;
					i++;
				}
				i = i + 2;
				if (arrayFatura[i].matches("\\d{1,3}(\\.\\d{3})*(,\\d{2})")) {
				    String subtotal = arrayFatura[i].replaceAll("\\.", "").replaceAll(",", "\\.");
				    total = total.add(new BigDecimal(subtotal));
				}
			}
		}
		return total;
	}
	
	private BigDecimal gerarTotalVerificacao(String[] arrayFatura) {
		BigDecimal total = new BigDecimal("0.00");
		
		for (int i = 0; i < arrayFatura.length; i++) {
			if (arrayFatura[i].equals("VALOR") && arrayFatura[i + 1].equals("DO") && arrayFatura[i + 2].equals("VIVO")) {
				while (!arrayFatura[i].equals("TOTAL")) {
				    i++;
				}
				i++;
				if (arrayFatura[i].matches("\\d{1,3}(\\.\\d{3})*(,\\d{2})")) {
				    String subtotal = arrayFatura[i].replaceAll("\\.", "").replaceAll(",", "\\.");
				    total = total.add(new BigDecimal(subtotal));
				}
			}
		}
		return total;
	}

	private boolean isFaturaUtilizada(Fatura fatura) {
		BigDecimal zero = new BigDecimal("0.00");
		if(fatura.getLinha().getResponsavel().isEmpty()) {
			if(fatura.getInternet().getCustoUsoDeIternet().compareTo(zero) == 0
					&& fatura.getInternet().getTotalUsoDeInternet().equals("0mb 0kb")
					&& fatura.getLigacoes().getCustoLigacoesLocais().compareTo(zero) == 0
					&& fatura.getLigacoes().getTempoLigacoesLocais().equals("0m00s")
					&& fatura.getLigacoes().getTempoLigacoesEmRoaming().equals("0m00s")
					&& fatura.getLigacoes().getTempoLigacoesInterurbanas().equals("0m00s")
					&& fatura.getLigacoes().getTempoLigacoesIntragrupo().equals("0m00s")
					&& fatura.getLigacoes().getTempoLigacoesNoExterior().equals("0m00s")
					&& fatura.getLigacoes().getTempoLigacoesRecebidasACobrar().equals("0m00s")
					&& fatura.getLigacoes().getTempoLigacoesServicosTerceiros().equals("0m00s")
					&& fatura.getLigacoes().getTempoCaixaPostal().equals("0m00s")
					&& fatura.getMensagens().getTotalMensagensNormaisEnviadas() == 0
					&& fatura.getMensagens().getTotalMensagensPlanoEnviadas() == 0
					&& fatura.getMensagens().getTotalMensagensOutrosServicosEnviadas() == 0
					&& fatura.getMensagens().getCustoMensagensNormaisEnviadas().compareTo(zero) == 0
					&& fatura.getMensagens().getCustoMensagensOutrosServicosEnviadas().compareTo(zero) == 0
					&& fatura.getMensagens().getCustoFotoTorpedosEnviados().compareTo(zero) == 0) {
				return false;
			}
		}
		return true;
	}
	
	private List<Linha> listarLinhas(int id) {
		EntityManager em = new JPAUtil().getEntityManager();
		try {
			LinhaDAO linhaDAO = new LinhaDAO(em);
			List<Linha> linhas = linhaDAO.listaPorId(id);
			return linhas;
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return null;
	}
	
	private void verificarNovasLinhas(List<String> listaFaturasIndividuais, List<Linha> linhasCadastradas) {
		EntityManager em = new JPAUtil().getEntityManager();
		
		try {
			LinhaDAO linhaDAO = new LinhaDAO(em);
			em.getTransaction().begin();
			
			if(linhasCadastradas.isEmpty()) {
				List<Linha> aux = new ArrayList<>();
				String arrayFatura[];
				String numero;
				for (String fatura : listaFaturasIndividuais) {
					arrayFatura = fatura.split("\\s+");
		            numero = getNumero(arrayFatura);
					Linha linha = new Linha(numero, "", this.cliente);
		            linhaDAO.adiciona(linha);
		            aux.add(linha);
				}
				linhasCadastradas = new ArrayList<>(aux);
			} else {
				List<Linha> aux = new ArrayList<>(linhasCadastradas);
				String arrayFatura[];
				String numero;
				Linha linha;
				for (String fatura : listaFaturasIndividuais) {
					arrayFatura = fatura.split("\\s+");
		            numero = getNumero(arrayFatura);
		            linha = new Linha(numero, "", this.cliente);
		            for (Linha l : linhasCadastradas) {//refatorar
						if(numero.equals(l.getNumero())) {
							linha = null;
						}
					}
		            if(linha != null) {
		            	linhaDAO.adiciona(linha);
		            	aux.add(linha);
		            }
				}
				linhasCadastradas = new ArrayList<>(aux);
			}
			
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}
	
	private void verificarPlanoCliente(List<String> listaFaturasIndividuais) {
		int totalFaturas = listaFaturasIndividuais.size();
		int totalLinhasPlano = cliente.getQuantidadeLinhas();
		if(totalLinhasPlano < 100 && totalFaturas >= 100) {
			throw new PlanoException(totalFaturas, totalLinhasPlano);
		}
		
		int centenaDePlanos = (totalLinhasPlano / 100) * 100;
		if(centenaDePlanos > totalFaturas || totalLinhasPlano + 99 < totalFaturas) {
			throw new PlanoException(totalFaturas, totalLinhasPlano);
		}
	}
	
	private Linha associarLinhaCliente(String numero, List<Linha> linhas) {
		for (Linha linha : linhas) {
			if(linha.getNumero().equals(numero)) {
				return linha;
			}
		}
		return new Linha("", "");
	}
	
	public List<Fatura> getListaFaturas() {
		return listaFaturas;
	}
	
	public List<Fatura> getListaFaturasNumerosNaoUtilizados() {
		return listaFaturasNumerosNaoUtilizados;
	}

	public List<Fatura> getListaFaturasComErro() {
		return listaFaturasComErro;
	}
}