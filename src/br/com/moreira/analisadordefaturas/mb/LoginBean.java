package br.com.moreira.analisadordefaturas.mb;


import java.math.BigDecimal;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.moreira.analisadordefaturas.dao.ClienteDAO;
import br.com.moreira.analisadordefaturas.dao.ConfiguracoesDAO;
import br.com.moreira.analisadordefaturas.filtro.ConfiguracoesPersonalizadas;
import br.com.moreira.analisadordefaturas.modelo.Cliente;
import br.com.moreira.analisadordefaturas.util.JPAUtil;


@ManagedBean
@SessionScoped
public class LoginBean {
	
	private EntityManager em;
	private ClienteDAO clienteDAO;
	private Cliente cliente = new Cliente();
	private ConfiguracoesPersonalizadas config;
	private ClienteLogado clienteLogado = new ClienteLogado();
	
	public LoginBean() {
		
	}

	public void initialize() {
		if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("senhaalt") != null) {
			if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("senhaalt").equals("sim")) 
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Sua senha foi alterada com sucesso!"));
		}
		String token = "";
		if(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("token") != null) {
			token = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("token").toString();
		
			em = new JPAUtil().getEntityManager();
			
			clienteDAO = new ClienteDAO(em);
			Cliente cliente = new Cliente();
			cliente.setToken(token);
			cliente = clienteDAO.buscaPorToken(cliente);
			
			if(!cliente.isClienteAtivo() && !cliente.isClienteAdimplente()) {
				cliente.setClienteAtivo(true);
				cliente.setClienteAdimplente(true);
				
				em.getTransaction().begin();
				
				clienteDAO.atualiza(cliente);
				
				em.getTransaction().commit();
				em.close();
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Sua conta foi ativada com sucesso!"));
			}
		}
	}
	
	public String efetuarLogin() {
		try {
			em = new JPAUtil().getEntityManager();
			clienteDAO = new ClienteDAO(em);
			this.cliente = clienteDAO.buscaPorLoginSenha(this.cliente);
			if(cliente != null) {
				if(!cliente.isClienteAtivo()) {
					this.cliente = new Cliente();
					clienteLogado = new ClienteLogado();
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cliente não foi ativado. Verifique seu email. Verifique também sua caixa de spam.", ""));
					return null;
				}
				if(!cliente.isClienteAdimplente()) {
					this.cliente = new Cliente();
					clienteLogado = new ClienteLogado();
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cliente está inadimplente.", ""));
					return null;
				}
				clienteLogado.setCliente(cliente);
				carregarConfiguracoesPersonalizadas();
				
				//em.getTransaction().begin();
				//DAO<LoggerAcesso> dao = new DAO<>(em, LoggerAcesso.class);
				//dao.adiciona(new LoggerAcesso(cliente));
				//em.getTransaction().commit();
				
				
			} else {
				this.cliente = new Cliente();
				clienteLogado = new ClienteLogado();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login e/ou senha inválidos.", ""));
				return null;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		
		return "loader?faces-redirect=true";
	}
	
	public String efetuarLogout() {
		clienteLogado = new ClienteLogado();
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		return "login?faces-redirect=true";
	}
	
	private void carregarConfiguracoesPersonalizadas() {
		ConfiguracoesDAO configDAO = new ConfiguracoesDAO(em);
		try {
			config = configDAO.buscaPorIdCliente(cliente.getId());
		} catch(NoResultException e) {
			em.getTransaction().begin();
			config = new ConfiguracoesPersonalizadas(cliente, false, 0, new BigDecimal("0.00"), new BigDecimal("0.00"));
			configDAO.adiciona(config);
			em.getTransaction().commit();
		}
	}
	
	public boolean isLogado() {
		return clienteLogado.isLogado();
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ConfiguracoesPersonalizadas getConfig() {
		return config;
	}

	public void setConfig(ConfiguracoesPersonalizadas config) {
		this.config = config;
	}
}
