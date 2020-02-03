package br.com.moreira.analisadordefaturas.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "linha")
public class Linha implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String responsavel;
	private String numero;
	private Cliente cliente;
	private List<Plano> listaPlanos = new ArrayList<Plano>();

	public Linha() {
	}

	public Linha(String numero, String responsavel) {
		this.numero = numero;
		this.responsavel = responsavel;
	}

	public Linha(String numero, String responsavel, Cliente cliente) {
		this.numero = numero;
		this.responsavel = responsavel;
		this.cliente = cliente;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(String responsavel) {
		this.responsavel = responsavel;
	}

	@Column(unique = true, nullable = false)
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@ManyToOne
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Column(name = "plano_id")
	@ManyToMany(fetch = FetchType.EAGER)
//	@JoinTable(name = "linha_plano", joinColumns={@JoinColumn(name="plano_id")})
	@JoinTable(name = "linha_plano")
	public List<Plano> getListaPlanos() {
		return listaPlanos;
	}

	public void setListaPlanos(List<Plano> listaPlanos) {
		this.listaPlanos = listaPlanos;
	}

	public void addPlano(Plano planoNormal) {
		this.listaPlanos.add(planoNormal);
	}

	@Override
	public String toString() {
		return id + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
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
		Linha other = (Linha) obj;
		if (id != other.id)
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
}
