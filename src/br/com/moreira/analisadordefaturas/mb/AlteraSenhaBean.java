package br.com.moreira.analisadordefaturas.mb;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import br.com.moreira.analisadordefaturas.dao.ClienteDAO;
import br.com.moreira.analisadordefaturas.util.Criptografo;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

@ManagedBean
public class AlteraSenhaBean {
	
	@ManagedProperty(value="#{loginBean}")
	private LoginBean loginBean;
	
	private String senhaAtual;
	private String novaSenha;
	private String confirmaSenha;
	
	public void alterarSenha() {
		String senhaCriptografada = Criptografo.criptografar(senhaAtual);
		
		if(!senhaCriptografada.equals(loginBean.getCliente().getSenha())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha atual incorreta.", ""));
			return;
		}
		if(!novaSenha.equals(confirmaSenha)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "As senha digitadas são incompatíveis.", ""));
			return;
		}
		
		EntityManager em = new JPAUtil().getEntityManager();
		try {
			ClienteDAO dao = new ClienteDAO(em);
			em.getTransaction().begin();
			
			loginBean.getCliente().setSenha(Criptografo.criptografar(novaSenha));
			dao.atualiza(loginBean.getCliente());
			
			em.getTransaction().commit();
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Sua senha foi alterada com sucesso."));
			
		} catch(Exception e) {
			
		} finally {
			em.close();
		}
				
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}
}
