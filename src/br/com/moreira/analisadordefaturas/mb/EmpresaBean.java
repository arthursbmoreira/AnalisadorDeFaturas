package br.com.moreira.analisadordefaturas.mb;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

@ManagedBean
public class EmpresaBean {
	
	@ManagedProperty(value="#{loaderBean}")
	private LoaderBean loaderBean;
	@ManagedProperty(value="#{loginBean}")
	private LoginBean loginBean;

	public EmpresaBean() {
	}
	
	public void initialize() {
		if(!loaderBean.getEmpresa().isTotalCorreto()) {
			String msg = "O total calculado pelo analisador de faturas está diferente do total encontrado no arquivo. Verifique a lista de faturas com erro no menu Faturas > Com erro.";
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", msg));
		}
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
}
