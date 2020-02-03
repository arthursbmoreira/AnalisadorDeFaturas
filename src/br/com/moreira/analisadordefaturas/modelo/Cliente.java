package br.com.moreira.analisadordefaturas.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cliente")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String nome;
	private String cpfCnpj;
	private String eMail;
	private Integer quantidadeLinhas;
	private String senha;
	private boolean clienteAtivo;
	private boolean clienteAdimplente;
	private Date dataCriacao;
	private int diaVencimento;
	private String token;
	private String senhaTemporaria;
	private List<Linha> linhas;
	private List<Plano> planos;
	private List<PlanoSubstituicao> planosSubstituicao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "nome")
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(unique = true, name = "doc_fiscal")
	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	@Column(unique = true, name = "email")
	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	@Column(name = "quantidade_linhas")
	public Integer getQuantidadeLinhas() {
		return quantidadeLinhas;
	}

	public void setQuantidadeLinhas(Integer quantidadeLinhas) {
		this.quantidadeLinhas = quantidadeLinhas;
	}

	@Column(name = "senha")
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Column(name = "cliente_ativo")
	public boolean isClienteAtivo() {
		return clienteAtivo;
	}

	public void setClienteAtivo(boolean clienteAtivo) {
		this.clienteAtivo = clienteAtivo;
	}

	@Column(name = "cliente_adimplente")
	public boolean isClienteAdimplente() {
		return clienteAdimplente;
	}

	public void setClienteAdimplente(boolean clienteAdimplente) {
		this.clienteAdimplente = clienteAdimplente;
	}

	@Column(name = "data_criacao")
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "dia_vencimento")
	public int getDiaVencimento() {
		return diaVencimento;
	}

	public void setDiaVencimento(int diaVencimento) {
		this.diaVencimento = diaVencimento;
	}

	@Column(name = "token")
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Column(name = "senha_temporaria")
	public String getSenhaTemporaria() {
		return senhaTemporaria;
	}

	public void setSenhaTemporaria(String senhaTemporaria) {
		this.senhaTemporaria = senhaTemporaria;
	}

	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
	public List<Linha> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<Linha> linhas) {
		this.linhas = linhas;
	}

	@OneToMany(mappedBy = "cliente")
	public List<Plano> getPlanos() {
		return planos;
	}

	public void setPlanos(List<Plano> planos) {
		this.planos = planos;
	}

	@OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
	public List<PlanoSubstituicao> getPlanosSubstituicao() {
		return planosSubstituicao;
	}

	public void setPlanosSubstituicao(List<PlanoSubstituicao> planosSubstituicao) {
		this.planosSubstituicao = planosSubstituicao;
	}
	
	@Override
	public String toString() {
		return "Cliente: " + nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpfCnpj == null) ? 0 : cpfCnpj.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (cpfCnpj == null) {
			if (other.cpfCnpj != null)
				return false;
		} else if (!cpfCnpj.equals(other.cpfCnpj))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}