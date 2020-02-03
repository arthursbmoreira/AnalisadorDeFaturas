package br.com.moreira.analisadordefaturas.analisadores;

import java.math.BigDecimal;

import br.com.moreira.analisadordefaturas.modelo.Mensagens;

public class AnalisadorMensagensVivo {
	
	public Mensagens analisar(String[] fatura) {
		Mensagens mensagens = new Mensagens(
				gerarTotalMensagensNormaisEnviadas(fatura), //totalMensagensNormaisEnviadas
				gerarCustoMensagensNormaisEnviadas(fatura), //custoMensagensNormaisEnviadas
				gerarCustoAdicionalPorMensagensEnviadas(fatura), //custoMensagensNormaisEnviadas
				gerarTotalMensagensPlanoEnviadas(fatura), //totalMensagensPlanoEnviadas
				gerarTotalFotoTorpedosEnviados(fatura), //totalFotosTorpedoEnviados
				gerarCustoFotoTorpedosEnviados(fatura), //custoFotoTorpedosEnviados
				gerarTotalMensagensOutrosServicosEnviadas(fatura), //totalMensagensOutrosServicosEnviadas
				gerarCustoMensagensOutrosServicosEnviadas(fatura),//custoMensagensOutrosServicosEnviadas
				null);//planos
		
		return mensagens;
		
	}
	
	private int gerarTotalMensagensNormaisEnviadas(String[] faturaArray) {
        int totalNormaisEnviadas = 0;
        int indice;
        for (int i = 0; i < faturaArray.length; i++) {
            if (faturaArray[i].equals("Torpedo") && faturaArray[i + 1].equals("SMS")) {
            	if(!faturaArray[i - 1].equals("Vivo")) {
	                if (!faturaArray[i + 3].equals("Outros") && !faturaArray[i + 4].equals("Serviços")) {
	                    indice = i;
	                    
	                    while (!faturaArray[indice].equals("Subtotal")) {
	                    	if(faturaArray[indice].equals("Vivo") && faturaArray[indice + 1].equals("Torpedo") && faturaArray[indice + 2].equals("SMS"))
	                    		totalNormaisEnviadas++;
	                        indice++;
	                    }
	                    i = indice;
	                }
            	}
            }
        }
        return totalNormaisEnviadas;
    }
	
	private BigDecimal gerarCustoMensagensNormaisEnviadas(String[] faturaArray) {
		BigDecimal total = new BigDecimal("0.00");
        int indice;
        for (int i = 0; i < faturaArray.length; i++) {
        	if (faturaArray[i].equals("Torpedo") && faturaArray[i + 1].equals("SMS")) {
            	if(!faturaArray[i - 1].equals("Vivo")) {
	                if (!faturaArray[i + 3].equals("Outros") && !faturaArray[i + 4].equals("Serviços")) {
		                indice = i;
		                while (!faturaArray[indice].equals("Subtotal")) {
		                    indice++;
		                }
		                indice = indice + 2;
		                if (faturaArray[indice].matches("[0-9]+\\,[0-9]{2}")) {
		                    String subtotal = faturaArray[indice].replaceAll("\\.", "").replaceAll(",", "\\.");
		                    total = total.add(new BigDecimal(subtotal));
		                }
		                i = indice;
	                }
            	}
        	}
        }
        return total;
    }
	//Adicional por Torpedos Enviados 
	private BigDecimal gerarCustoAdicionalPorMensagensEnviadas(String[] faturaArray) {
		BigDecimal total = new BigDecimal("0.00");
        int indice;
        for (int i = 0; i < faturaArray.length; i++) {
        	if (faturaArray[i].equals("Adicional") && faturaArray[i + 1].equals("por") && faturaArray[i + 2].equals("Torpedos") && faturaArray[i + 3].equals("Enviados")) {
                indice = i;
                while (!faturaArray[indice].equals("Subtotal")) {
                    indice++;
                }
                indice = indice + 2;
                if (faturaArray[indice].matches("[0-9]+\\,[0-9]{2}")) {
                    String subtotal = faturaArray[indice].replaceAll("\\.", "").replaceAll(",", "\\.");
                    total = total.add(new BigDecimal(subtotal));
                }
                i = indice;
        	}
        }
        return total;
    }
	
	private int gerarTotalMensagensPlanoEnviadas(String[] faturaArray) {
		int totalPlanoEnviadas = 0;
        int indice;
        for (int i = 0; i < faturaArray.length; i++) {
            if (faturaArray[i].equals("Torpedo") && faturaArray[i + 1].equals("SMS")) {
            	if(!faturaArray[i - 1].equals("Vivo")) {
	                if (!faturaArray[i + 3].equals("Outros") && !faturaArray[i + 4].equals("Serviços")) {
	                    indice = i;
	                    
	                    while (!faturaArray[indice].equals("Subtotal")) {
	                    	if(faturaArray[indice].equals("GRÁTIS-SMS"))
	                    		totalPlanoEnviadas++;
	                        indice++;
	                    }
	                    i = indice;
	                }
            	}
            }
        }
        return totalPlanoEnviadas;
    }
	
	private int gerarTotalFotoTorpedosEnviados(String[] faturaArray) {
		int totalFotoTorpedosEnviados = 0;
        int indice;
        for (int i = 0; i < faturaArray.length; i++) {
        	if (faturaArray[i].equals("Foto") && faturaArray[i + 1].equals("Torpedo") && faturaArray[i + 2].equals("MMS")) {
	                    indice = i;
	                    
	                    while (!faturaArray[indice].equals("Subtotal")) {
	                        indice++;
	                    }
	                    indice++;
	                    totalFotoTorpedosEnviados += Integer.parseInt(faturaArray[indice]);
	                    i = indice;
            }
        }
        return totalFotoTorpedosEnviados;
    }
	
	private BigDecimal gerarCustoFotoTorpedosEnviados(String[] faturaArray) {
		BigDecimal total = new BigDecimal("0.00");
        int indice;
        for (int i = 0; i < faturaArray.length; i++) {
            if (faturaArray[i].equals("Foto") && faturaArray[i + 1].equals("Torpedo") && faturaArray[i + 2].equals("MMS")) {
                indice = i;
                while (!faturaArray[indice].equals("Subtotal")) {
                    indice++;
                }
                indice = indice + 2;
                if (faturaArray[indice].matches("[0-9]+\\,[0-9]{2}")) {
                    String subtotal = faturaArray[indice].replaceAll("\\.", "").replaceAll(",", "\\.");
                    total = total.add(new BigDecimal(subtotal));
                }
                i = indice;
            }
        }
        return total;
    }
	
	private int gerarTotalMensagensOutrosServicosEnviadas(String[] faturaArray) {
		int totalMensagensEnviadas = 0;
        int indice;
        for (int i = 0; i < faturaArray.length; i++) {
        	if (faturaArray[i].equals("Torpedo") && faturaArray[i + 1].equals("SMS") && faturaArray[i + 3].equals("Outros") && faturaArray[i + 4].equals("Serviços")) {
	                    indice = i;
	                    
	                    while (!faturaArray[indice].equals("Subtotal")) {
	                        indice++;
	                    }
	                    indice++;
	                    totalMensagensEnviadas += Integer.parseInt(faturaArray[indice]);
	                    i = indice;
            }
        }
        return totalMensagensEnviadas;
    }
	
	private BigDecimal gerarCustoMensagensOutrosServicosEnviadas(String[] faturaArray) {
		BigDecimal total = new BigDecimal("0.00");
        int indice;
        for (int i = 0; i < faturaArray.length; i++) {
            if (faturaArray[i].equals("Torpedo") && faturaArray[i + 1].equals("SMS") && faturaArray[i + 3].equals("Outros") && faturaArray[i + 4].equals("Serviços")) {
                indice = i;
                while (!faturaArray[indice].equals("Subtotal")) {
                    indice++;
                }
                indice = indice + 2;
                if (faturaArray[indice].matches("[0-9]+\\,[0-9]{2}")) {
                    String subtotal = faturaArray[indice].replaceAll("\\.", "").replaceAll(",", "\\.");
                    total = total.add(new BigDecimal(subtotal));
                }
                i = indice;
            }
        }
        return total;
    }
}
