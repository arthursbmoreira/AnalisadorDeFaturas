package br.com.moreira.analisadordefaturas.mb;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.primefaces.context.RequestContext;

import br.com.moreira.analisadordefaturas.dao.ClienteDAO;
import br.com.moreira.analisadordefaturas.modelo.Cliente;
import br.com.moreira.analisadordefaturas.modelo.Mail;
import br.com.moreira.analisadordefaturas.util.Criptografo;
import br.com.moreira.analisadordefaturas.util.GeradorDeSenha;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

@ManagedBean
@ViewScoped
public class RecuperaSenhaBean {
	EntityManager em;
	Cliente cliente = new Cliente();
	private String confirmaSenha;
	
	public void initialize() {
		if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("mail") != null) {
			cliente.seteMail(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("mail"));
		}
	}
	
	public void enviarNovaSenha() {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		
		ClienteDAO dao = new ClienteDAO(em);
		cliente = dao.buscaPorEmail(cliente);
		
		try {
			if(cliente == null) {
				cliente = new Cliente();
				throw new RuntimeException("Email não cadastrado.");
			}
			
			cliente.setSenhaTemporaria(GeradorDeSenha.gerarNovaSenha());
			Mail.enviarEmailRecuperacaoDeSenha(cliente);
			cliente.setSenhaTemporaria(Criptografo.criptografar(cliente.getSenhaTemporaria()));
			
			dao.atualiza(cliente);
			em.getTransaction().commit();
			
			RequestContext rc = RequestContext.getCurrentInstance();
			
		    rc.execute("PF('recuperacao').hide()");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso.", "Uma nova senha foi enviada para o seu email. Lembre-se de verificar, também, sua caixa de spam."));
		} catch(RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro.", e.getMessage()));
		} finally {
			em.close();
		}
	}
	
	public String alterarSenha() {
		try {
			em = new JPAUtil().getEntityManager();
			
			if(!cliente.getSenha().equals(confirmaSenha))
				throw new RuntimeException("As senha digitadas são incomptatíveis.");
			
			String novaSenha = cliente.getSenha();
			
			ClienteDAO dao = new ClienteDAO(em);
			em.getTransaction().begin();
			
			cliente = dao.buscaPorLoginSenhaTemporaria(cliente);
			if(cliente == null) {
				cliente = new Cliente();
				throw new RuntimeException("Senha atual inválida.");
			} else if(!cliente.getSenhaTemporaria().isEmpty()) {
				cliente.setSenhaTemporaria("");
				cliente.setSenha(Criptografo.criptografar(novaSenha));
				dao.atualiza(cliente);
				
				em.getTransaction().commit();
				
				return "login?faces-redirect=true&senhaalt=sim";
			}
		} catch(RuntimeException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
		} finally {
			em.close();
		}
		return null;
	}
	
	public Cliente getCliente() {
		return cliente;
	}
	public String getConfirmaSenha() {
		return confirmaSenha;
	}
	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}
}
