package br.com.moreira.analisadordefaturas.mb;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.primefaces.event.CellEditEvent;

import br.com.moreira.analisadordefaturas.dao.LinhaDAO;
import br.com.moreira.analisadordefaturas.modelo.Fatura;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

@ManagedBean
@RequestScoped
public class CadastroResponsavelBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	EntityManager em;
	private LinhaDAO linhaDAO;
	@ManagedProperty(value="#{loaderBean}")
	private LoaderBean loaderBean;
	
	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        
        if(!newValue.equals(oldValue)) {
	        Fatura fatura = loaderBean.getListaFaturas().get(event.getRowIndex());
	        
	        em = new JPAUtil().getEntityManager();
	        em.getTransaction().begin();
	        
	        linhaDAO = new LinhaDAO(em);
	        linhaDAO.atualiza(fatura.getLinha());
	        
	        em.getTransaction().commit();
	        em.close();        
	        
	        if(newValue != null && !newValue.equals(oldValue)) {
	            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Responsável alterado com sucesso.", "Responsável: " + newValue);
	            FacesContext.getCurrentInstance().addMessage(null, msg);
	        }
        }
    }
	
	public String removerResponsaveis() {
		em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        linhaDAO = new LinhaDAO(em);
		for (Fatura fatura : loaderBean.getListaFaturas()) {
			fatura.getLinha().setResponsavel("");
	        linhaDAO.atualiza(fatura.getLinha());
		}
		em.getTransaction().commit();
        em.close();
        
        return "cadastroResponsavel?faces-redirect=true";
	}

	public LoaderBean getLoaderBean() {
		return loaderBean;
	}

	public void setLoaderBean(LoaderBean loaderBean) {
		this.loaderBean = loaderBean;
	}
}
