package br.com.moreira.analisadordefaturas.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;

import br.com.moreira.analisadordefaturas.dao.LinhaDAO;
import br.com.moreira.analisadordefaturas.modelo.Fatura;
import br.com.moreira.analisadordefaturas.modelo.Internet;
import br.com.moreira.analisadordefaturas.modelo.Ligacoes;
import br.com.moreira.analisadordefaturas.modelo.Mensagens;
import br.com.moreira.analisadordefaturas.modelo.Plano;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

@ManagedBean
@SessionScoped
public class FaturasBean {
	
	private Fatura fatura = new Fatura(new Ligacoes(), new Mensagens(), new Internet());
	
	private Plano planoNormal = new Plano();
	
	@ManagedProperty(value="#{loaderBean}")
	private LoaderBean loaderBean;
	
	@ManagedProperty(value="#{loginBean}")
	private LoginBean loginBean;
	
	private boolean planosNaImpressao = true;
	
	private boolean descontoNaImpressao = false;
	
	public FaturasBean() {
		
	}
	
	public void cadastrarResponsavel() {
		EntityManager em = new JPAUtil().getEntityManager();
        try {
			em.getTransaction().begin();
	        
	        LinhaDAO linhaDAO = new LinhaDAO(em);
	        linhaDAO.atualiza(fatura.getLinha());
	        
	        em.getTransaction().commit();
        } catch(Exception e) {
        	
        } finally {
        	em.close();
        }
	}
	
	public void cadastrarResponsavelLinhaSemUso() {
		EntityManager em = new JPAUtil().getEntityManager();
        try {
			em.getTransaction().begin();
	        
	        LinhaDAO linhaDAO = new LinhaDAO(em);
	        linhaDAO.atualiza(fatura.getLinha());
	        
	        em.getTransaction().commit();
	        
	        loaderBean.getListaFaturas().add(fatura);
	        loaderBean.getListaFaturasNumerosNaoUtilizados().remove(fatura);
        } catch(Exception e) {
        	
        } finally {
        	em.close();
        }
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}
	
	public Plano getPlano() {
		return planoNormal;
	}

	public void setPlano(Plano planoNormal) {
		this.planoNormal = planoNormal;
	}

	public LoaderBean getLoaderBean() {
		return loaderBean;
	}

	public void setLoaderBean(LoaderBean loaderBean) {
		this.loaderBean = loaderBean;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public boolean isPlanosNaImpressao() {
		return planosNaImpressao;
	}

	public void setPlanosNaImpressao(boolean planosNaImpressao) {
		this.planosNaImpressao = planosNaImpressao;
	}

	public boolean isDescontoNaImpressao() {
		return descontoNaImpressao;
	}

	public void setDescontoNaImpressao(boolean descontoNaImpressao) {
		this.descontoNaImpressao = descontoNaImpressao;
	}
}
