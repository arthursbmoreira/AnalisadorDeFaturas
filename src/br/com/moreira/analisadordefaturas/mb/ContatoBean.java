package br.com.moreira.analisadordefaturas.mb;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import br.com.moreira.analisadordefaturas.modelo.Cliente;
import br.com.moreira.analisadordefaturas.modelo.Mail;

@ManagedBean
public class ContatoBean {
	
	@ManagedProperty(value="#{loginBean.cliente}")
	private Cliente cliente;
	
	private String nome;
	private String email;
	private String assunto;
	private String mensagem;
	
	public void initialize() {
		email = cliente != null ? cliente.geteMail() : "";
	}

	public void enviarEmail() {
		try {
			this.mensagem = this.mensagem.replaceAll("<", "").replaceAll(">", "");
			String remetente = "Nome: " + nome + "<br>Email: " + email + "<br>";
			String mensagem = remetente + "Mensagem:<br><br>" +this.mensagem;
			
			Mail.enviarEmailContato(assunto, mensagem);
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Sua mensagem foi enviada com sucesso. Entraremos em contato o mais rápido possível.");
			FacesContext.getCurrentInstance().addMessage("", message);
			
		} catch(Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro no envio da mensagem.");
			FacesContext.getCurrentInstance().addMessage("", message);
		}		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
}
