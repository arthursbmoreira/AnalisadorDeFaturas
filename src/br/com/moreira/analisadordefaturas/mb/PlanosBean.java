package br.com.moreira.analisadordefaturas.mb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import br.com.moreira.analisadordefaturas.dao.LinhaDAO;
import br.com.moreira.analisadordefaturas.dao.PlanoDAO;
import br.com.moreira.analisadordefaturas.dao.PlanoSubstituicaoDAO;
import br.com.moreira.analisadordefaturas.modelo.Fatura;
import br.com.moreira.analisadordefaturas.modelo.Linha;
import br.com.moreira.analisadordefaturas.modelo.Plano;
import br.com.moreira.analisadordefaturas.modelo.PlanoSubstituicao;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

@ManagedBean
@ViewScoped
public class PlanosBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@ManagedProperty(value="#{loginBean}")
	private LoginBean loginBean;
	
	@ManagedProperty(value="#{loaderBean}")
	private LoaderBean loaderBean;
	
	private Plano plano = new Plano();
	private List<Plano> listaPlanos = new ArrayList<>();
	
	private Plano planoAssociacao;
	private Linha linhaAssociacao;
	
	private Plano planoDesassociacaoAux;
	private Plano planoDesassociacao;
	private Linha linhaDesassociacao;
	
	private PlanoSubstituicao planoSubstituicao = new PlanoSubstituicao();
	private List<PlanoSubstituicao> listaPlanosSubstituicao = new ArrayList<>();
	
	private EntityManager em;
	private PlanoDAO planoDAO;
	private PlanoSubstituicaoDAO planoSubstituicaoDAO;
	private LinhaDAO linhaDAO;
	
	@PostConstruct
	public void initialize() {
		buscarPlanos();
	}
	
	public void buscarPlanos() {
        try {
        	getEm();
        	
			this.planoDAO = new PlanoDAO(em);
			listaPlanos = planoDAO.lista(loginBean.getCliente());
			
			this.planoSubstituicaoDAO = new PlanoSubstituicaoDAO(em);
			listaPlanosSubstituicao = planoSubstituicaoDAO.lista(loginBean.getCliente());
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {
        	closeEm();
        }
	}
	
	public void salvarPlano() {
        try {
        	getEm();
			em.getTransaction().begin();
	        
			plano.setCliente(loginBean.getCliente());
			
			this.planoDAO = new PlanoDAO(em);
			this.planoDAO.adiciona(plano);
	
	        em.getTransaction().commit();
	        
	        listaPlanos = this.planoDAO.lista(loginBean.getCliente());
	        
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Plano salvo com sucesso.");
			FacesContext.getCurrentInstance().addMessage("", message);
	        
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {
        	closeEm();
        	this.plano = new Plano();
        }
	}
	
	public void salvarPlanoSubstituicao() {
        try {
        	getEm();
			em.getTransaction().begin();
	        
			planoSubstituicao.setCliente(loginBean.getCliente());
			
			this.planoSubstituicaoDAO = new PlanoSubstituicaoDAO(em);
			this.planoSubstituicaoDAO.adiciona(planoSubstituicao);
	
	        em.getTransaction().commit();
	        
	        listaPlanosSubstituicao = planoSubstituicaoDAO.lista(loginBean.getCliente());
	        loginBean.getCliente().setPlanosSubstituicao(listaPlanosSubstituicao);
	        
	        String mensagem = "Plano salvo com sucesso. Os valores tomarão efeito assim que a fatura for analisada novamente.";
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem);
			FacesContext.getCurrentInstance().addMessage("", message);
	        
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {
        	closeEm();
        	this.planoSubstituicao = new PlanoSubstituicao();
        }
	}
	
	public void removerPlano(Plano plano) {
        try {
        	getEm();
			em.getTransaction().begin();
	        
			this.planoDAO = new PlanoDAO(em);
			this.planoDAO.remove(em.merge(plano));
	
	        em.getTransaction().commit();
	        
	        listaPlanos = this.planoDAO.lista(loginBean.getCliente());
	        
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Plano removido com sucesso.");
			FacesContext.getCurrentInstance().addMessage("", message);
	        
        }catch(PersistenceException e) {
        	FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Este plano está associado à uma ou mais linhas. Desfaça as associações antes de removê-lo.");
			FacesContext.getCurrentInstance().addMessage("", message);
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {
        	closeEm();
        }
	}
	
	public void removerPlanoSubstituicao(PlanoSubstituicao planoSubstituicao) {
        try {
        	getEm();
			em.getTransaction().begin();
	        
			this.planoSubstituicaoDAO = new PlanoSubstituicaoDAO(em);
			this.planoSubstituicaoDAO.remove(em.merge(planoSubstituicao));
	
	        em.getTransaction().commit();
	        
	        listaPlanosSubstituicao = this.planoSubstituicaoDAO.lista(loginBean.getCliente());
	        loginBean.getCliente().setPlanosSubstituicao(listaPlanosSubstituicao);
	        
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Plano removido com sucesso.");
			FacesContext.getCurrentInstance().addMessage("", message);
	        
        } catch(Exception e) {
        	e.printStackTrace();
        } finally {
        	closeEm();
        }
	}
	
	public void associarPlano() {
		if(this.planoAssociacao != null && this.linhaAssociacao != null) {
	        try {
	        	getEm();
				em.getTransaction().begin();
				
				linhaAssociacao.getListaPlanos().add(this.planoAssociacao);
				
				this.linhaDAO = new LinhaDAO(em);
				this.linhaDAO.atualiza(linhaAssociacao);
				
				em.getTransaction().commit();
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Plano associado com sucesso.");
				FacesContext.getCurrentInstance().addMessage("", message);
	        } catch(Exception e) {
	        	e.printStackTrace();
	        } finally {
	        	closeEm();
	        }
	        addPlanoListaLoaderBean(loaderBean.getListaFaturas(), linhaAssociacao, planoAssociacao);
		}
	}
	
	private void addPlanoListaLoaderBean(List<Fatura> lista, Linha linha, Plano plano) {
		for (Fatura f : lista) {
			if(f.getLinha().equals(linha)) {
				f.getLinha().getListaPlanos().add(plano);
				loginBean.getConfig().aplicarConfiguraçõesPorLinha(f);
			}
		}
	}
	
	public void setarPlanoDessasociacao(AjaxBehaviorEvent event) {
		this.planoDesassociacaoAux = this.planoDesassociacao;
	}
	
	public void removerAssociacao(Fatura fatura) {
		if(planoDesassociacaoAux != null) {
			try {
				getEm();
				
				this.linhaDAO = new LinhaDAO(em);
				
				linhaDesassociacao =  removerPlanoDessasociacao(planoDesassociacaoAux, this.linhaDAO.busca(fatura.getLinha().getId()));
	        
				em.getTransaction().begin();
		        
				this.linhaDAO.atualiza(linhaDesassociacao);
				
				em.getTransaction().commit();
				
				removerPlanoListaLoaderBean(loaderBean.getListaFaturas(), linhaDesassociacao, planoDesassociacaoAux);
				
				planoDesassociacao = null;
				planoDesassociacaoAux = null;
				
				
				FacesContext.getCurrentInstance()
					.addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Plano desassociado com sucesso."));
	        } catch(Exception e) {
	        	e.printStackTrace();
	        } finally {
	        	closeEm();
	        }
		}
	}
	
	private Linha removerPlanoDessasociacao(Plano plano, Linha linha) {
		List<Plano> planos = linha.getListaPlanos();
		for(Plano p : linha.getListaPlanos()) {
			if(p.equals(plano)) {
				planos.remove(p);
				break;
			}
		}
		linha.setListaPlanos(planos);
		return linha;
	}
	
	private void removerPlanoListaLoaderBean(List<Fatura> lista, Linha linha, Plano plano) {
		for (Fatura f : lista) {
			if(f.getLinha().equals(linha)) {
				f.getLinha().getListaPlanos().remove(plano);
				loginBean.getConfig().aplicarConfiguraçõesPorLinha(f);
				break;
			}
		}
	}
	
	private void getEm() {
		this.em = new JPAUtil().getEntityManager();
	}
	
	private void closeEm() {
		this.em.close();
	}
	
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
	
	public LoaderBean getLoaderBean() {
		return loaderBean;
	}

	public void setLoaderBean(LoaderBean loaderBean) {
		this.loaderBean = loaderBean;
	}

	public Plano getPlano() {
		return plano;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}

	public List<Plano> getListaPlanos() {
		return listaPlanos;
	}

	public void setListaPlanos(List<Plano> listaPlanos) {
		this.listaPlanos = listaPlanos;
	}

	public Plano getPlanoAssociacao() {
		return planoAssociacao;
	}

	public void setPlanoAssociacao(Plano planoAssociacao) {
		this.planoAssociacao = planoAssociacao;
	}

	public Linha getLinhaAssociacao() {
		return linhaAssociacao;
	}

	public void setLinhaAssociacao(Linha linhaAssociacao) {
		this.linhaAssociacao = linhaAssociacao;
	}

	public Plano getPlanoDesassociacao() {
		return planoDesassociacao;
	}

	public void setPlanoDesassociacao(Plano planoDesassociacao) {
		this.planoDesassociacao = planoDesassociacao;
	}

	public PlanoSubstituicao getPlanoSubstituicao() {
		return planoSubstituicao;
	}

	public void setPlanoSubstituicao(PlanoSubstituicao planoSubstituicao) {
		this.planoSubstituicao = planoSubstituicao;
	}

	public List<PlanoSubstituicao> getListaPlanosSubstituicao() {
		return listaPlanosSubstituicao;
	}

	public void setListaPlanosSubstituicao(List<PlanoSubstituicao> listaPlanosSubstituicao) {
		this.listaPlanosSubstituicao = listaPlanosSubstituicao;
	}
}