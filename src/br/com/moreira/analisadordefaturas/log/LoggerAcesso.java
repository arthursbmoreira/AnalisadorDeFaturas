package br.com.moreira.analisadordefaturas.log;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.moreira.analisadordefaturas.modelo.Cliente;

@Entity
@Table(name = "log_acesso")
public class LoggerAcesso implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Cliente cliente;
	private Calendar dataHora;

	public LoggerAcesso() {
	}

	public LoggerAcesso(Cliente cliente) {
		super();
		this.cliente = cliente;
		this.dataHora = Calendar.getInstance();
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

	public Calendar getDataHora() {
		return dataHora;
	}

	public void setDataHora(Calendar dataHora) {
		this.dataHora = dataHora;
	}
}
