package br.com.moreira.analisadordefaturas.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import br.com.moreira.analisadordefaturas.dao.LinhaDAO;
import br.com.moreira.analisadordefaturas.modelo.Fatura;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

@ManagedBean
@ViewScoped
public class FaturasNaoUtilizadasBean {
	
	private Fatura fatura = new Fatura();
	@ManagedProperty(value="#{loaderBean}")
	private LoaderBean loaderBean;
	
	public FaturasNaoUtilizadasBean() {
		
	}
	
	public void cadastrarResponsavel() {
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

	public LoaderBean getLoaderBean() {
		return loaderBean;
	}

	public void setLoaderBean(LoaderBean loaderBean) {
		this.loaderBean = loaderBean;
	}
}
