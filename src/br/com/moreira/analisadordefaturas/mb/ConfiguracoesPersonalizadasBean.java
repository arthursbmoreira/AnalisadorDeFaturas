package br.com.moreira.analisadordefaturas.mb;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import br.com.moreira.analisadordefaturas.dao.ConfiguracoesDAO;
import br.com.moreira.analisadordefaturas.filtro.ConfiguracoesPersonalizadas;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

@ManagedBean
@SessionScoped
public class ConfiguracoesPersonalizadasBean {
	
	@ManagedProperty(value="#{loginBean.config}")
	private ConfiguracoesPersonalizadas configuracoes;
	@ManagedProperty(value="#{loaderBean}")
	private LoaderBean loaderBean;
	
	public ConfiguracoesPersonalizadasBean() {
	}

	public ConfiguracoesPersonalizadas getConfiguracoes() {
		return configuracoes;
	}

	public void setConfiguracoes(ConfiguracoesPersonalizadas configuracoes) {
		this.configuracoes = configuracoes;
	}

	public void setLoaderBean(LoaderBean loaderBean) {
		this.loaderBean = loaderBean;
	}

	public void ativarConfiguracoes() {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		ConfiguracoesDAO configDAO = new ConfiguracoesDAO(em);
		configDAO.atualiza(configuracoes);
		em.getTransaction().commit();
		em.close();
		
		FacesMessage message;		
		if(configuracoes.isAtivo()) 
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "As configurções personalizadas foram ativadas.");
		 else 
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "As configurações personalizadas foram desativadas.");
		
		FacesContext.getCurrentInstance().addMessage("", message);
	}
	
	public void salvarConfiguracoes() {
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		ConfiguracoesDAO configDAO = new ConfiguracoesDAO(em);
		configDAO.atualiza(configuracoes);
		em.getTransaction().commit();
		em.close();
		try {
			loaderBean.construir();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "As configurações foram salvas com sucesso.");
			FacesContext.getCurrentInstance().addMessage("", message);
		} catch(NullPointerException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Selecione um arquivo para realizar esta operação.", "");
			FacesContext.getCurrentInstance().addMessage("", message);
		}
		
	}
}
