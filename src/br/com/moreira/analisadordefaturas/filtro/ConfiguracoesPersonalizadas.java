package br.com.moreira.analisadordefaturas.filtro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

import br.com.moreira.analisadordefaturas.modelo.Cliente;
import br.com.moreira.analisadordefaturas.modelo.Fatura;
import br.com.moreira.analisadordefaturas.modelo.Plano;

@Entity
@Table(name = "configuracoes_personalizadas")
public class ConfiguracoesPersonalizadas implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Cliente cliente;
	private boolean isAtivo;
	private int tempoLigacoes;
	private BigDecimal custoMinuto;
	private BigDecimal valorBase;

	public ConfiguracoesPersonalizadas() {
	}

	public ConfiguracoesPersonalizadas(Cliente cliente, boolean isAtivo, int tempoLigacoes, BigDecimal custoMinuto, BigDecimal valorBase) {
		super();
		this.cliente = cliente;
		this.isAtivo = isAtivo;
		this.tempoLigacoes = tempoLigacoes;
		this.custoMinuto = custoMinuto;
		this.valorBase = valorBase;
	}

	public List<Fatura> aplicarConfiguracoesPersonalizadas(List<Fatura> listaFaturas) {
		for (Fatura f : listaFaturas) {
			aplicarConfiguraçõesPorLinha(f);
		}
		return listaFaturas;
	}

	public void aplicarConfiguraçõesPorLinha(Fatura fatura) {
		String tempoLigacoes = retornarTempoLigacoes(fatura.getLigacoes().getTempoLigacoesLocais(), fatura.getLigacoes().getTempoCaixaPostal(), fatura.getLigacoes().getTempoLigacoesRecebidasACobrar());

		fatura.getLigacoes().setTempoLigacoesPersonalizadas(tempoLigacoes);

		int min = Integer.parseInt(tempoLigacoes.replaceAll("\\.", "").split("m")[0]);
		if (min > this.tempoLigacoes) {
			int excedente = min - this.tempoLigacoes;
			fatura.getLigacoes().setTempoExcedenteLigacoesPersonalizadas(excedente + "m");
			fatura.getLigacoes().setCustoLigacoesPersonalizadas(this.custoMinuto.multiply(new BigDecimal(excedente)));
		} else {
			fatura.getLigacoes().setCustoLigacoesPersonalizadas(new BigDecimal("0.00"));
		}

		BigDecimal custoLigacoesNaoIncluidas = fatura.getLigacoes().getCustoLigacoesInterurbanas()
																   .add(fatura.getLigacoes().getCustoLigacoesNoExterior())
																   .add(fatura.getLigacoes().getCustoLojaDeServicosVivo())
																   .add(fatura.getLigacoes().getCustoLigacoesServicosTerceiros());
		fatura.getLigacoes().setCustoLigacoesNaoIncluidas(custoLigacoesNaoIncluidas);

		fatura.setValorBase(this.valorBase);

		BigDecimal custoTotalFaturaPersonalizada = this.valorBase
													.add(fatura.getLigacoes().getCustoLigacoesPersonalizadas())
													.add(fatura.getLigacoes().getCustoLigacoesInterurbanas())
													.add(fatura.getLigacoes().getCustoLigacoesNoExterior())
													.add(fatura.getLigacoes().getCustoLojaDeServicosVivo())
													.add(fatura.getLigacoes().getCustoLigacoesServicosTerceiros())
													.add(fatura.getMensagens().getCustoMensagensNormaisEnviadas())
													.add(fatura.getMensagens().getCustoMensagensOutrosServicosEnviadas())
													.add(fatura.getMensagens().getCustoFotoTorpedosEnviados())
													.add(fatura.getInternet().getCustoUsoDeIternet())
													.add(fatura.getValorSubstituicao());

		BigDecimal custoTotalFaturaComDescontoPersonalizada = custoTotalFaturaPersonalizada.subtract(fatura.getDescontos());

		fatura.setCustoTotalFaturaPersonalizada(custoTotalFaturaPersonalizada.add(fatura.getCustoServicosContratados()));
		fatura.setCustoTotalFaturaComDescontoPersonalizada(custoTotalFaturaComDescontoPersonalizada.add(fatura.getCustoServicosContratados()));

		BigDecimal custoTotalPlanos = new BigDecimal("0.00");
		for (Plano planoNormal : fatura.getLinha().getListaPlanos()) {
			custoTotalPlanos = custoTotalPlanos.add(planoNormal.getValor());
		}

		fatura.setCustoTotalPlanos(custoTotalPlanos);

		if (custoTotalPlanos.compareTo(BigDecimal.ZERO) != 0) {
			fatura.setCustoTotalFaturaPersonalizada(fatura.getCustoTotalFaturaPersonalizada().add(custoTotalPlanos));
			fatura.setCustoTotalFaturaComDescontoPersonalizada(fatura.getCustoTotalFaturaComDescontoPersonalizada().add(custoTotalPlanos));
		}
	}

	private String retornarTempoLigacoes(String... tempos) {
		int minutos = 0;
		int segundos = 0;
		for (String s : tempos) {
			String[] minSeg = s.replaceAll("\\.", "").split("m");
			int min = Integer.parseInt(minSeg[0]);
			int seg = Integer.parseInt((minSeg[1].split("s"))[0]);
			minutos += min;
			segundos += seg;
		}

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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@OneToOne
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public boolean isAtivo() {
		return isAtivo;
	}

	public void setAtivo(boolean isAtivo) {
		this.isAtivo = isAtivo;
	}

	@Column(name = "tempo_ligacoes")
	@Min(value = 0, message = "O tempo mínimo deve ser maior que 0.")
	public int getTempoLigacoes() {
		return tempoLigacoes;
	}

	public void setTempoLigacoes(int tempoLigacoes) {
		this.tempoLigacoes = tempoLigacoes;
	}

	@Column(name = "custo_minuto")
	@DecimalMin(value = "0.00", message = "O custo por minuto informado deve ser maior que 0.")
	public BigDecimal getCustoMinuto() {
		return custoMinuto;
	}

	public void setCustoMinuto(BigDecimal custoMinuto) {
		this.custoMinuto = custoMinuto;
	}

	@Column(name = "valor_base")
	@DecimalMin(value = "0.00", message = "O valor base informado deve ser maior que 0.")
	public BigDecimal getValorBase() {
		return valorBase;
	}

	public void setValorBase(BigDecimal valorBase) {
		this.valorBase = valorBase;
	}

}