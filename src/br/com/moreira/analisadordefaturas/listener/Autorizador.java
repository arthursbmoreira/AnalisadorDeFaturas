package br.com.moreira.analisadordefaturas.listener;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.com.moreira.analisadordefaturas.mb.LoginBean;

public class Autorizador implements PhaseListener {

	private static final long serialVersionUID = -8570478799432860907L;

	@Override
	public void afterPhase(PhaseEvent event) {
		
		FacesContext context = event.getFacesContext();
		
		if("/recuperacao-de-senha.xhtml".equals(context.getViewRoot().getViewId()))
			return;
		if("/ajuda.xhtml".equals(context.getViewRoot().getViewId()))
			return;
		if("/contato.xhtml".equals(context.getViewRoot().getViewId()))
			return;
		
		LoginBean loginBean = context.getApplication().evaluateExpressionGet(context, "#{loginBean}", LoginBean.class);
		
		if(loginBean.isLogado()) {
			if("/login.xhtml".equals(context.getViewRoot().getViewId())) {
				NavigationHandler handler = context.getApplication().getNavigationHandler();
				handler.handleNavigation(context, null, "loader?faces-redirect=true");
				
				context.renderResponse();
			}
		}
		
		if("/login.xhtml".equals(context.getViewRoot().getViewId()))
			return;
			
		if(!loginBean.isLogado()) {
			NavigationHandler handler = context.getApplication().getNavigationHandler();
			handler.handleNavigation(context, null, "login?faces-redirect=true");
			
			context.renderResponse();
		}
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
