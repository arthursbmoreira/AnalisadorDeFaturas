package br.com.moreira.analisadordefaturas.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.moreira.analisadordefaturas.modelo.Cliente;

@ManagedBean
@SessionScoped
public class ClienteLogado {
	
	private Cliente cliente;
	
	public boolean isLogado() {
		return cliente != null;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}
