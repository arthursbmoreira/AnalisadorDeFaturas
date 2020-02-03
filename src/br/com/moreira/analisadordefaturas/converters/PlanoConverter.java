package br.com.moreira.analisadordefaturas.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.com.moreira.analisadordefaturas.dao.PlanoDAO;
import br.com.moreira.analisadordefaturas.modelo.Plano;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

@FacesConverter(forClass=Plano.class)
public class PlanoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value != null && !value.equals("")) {
			EntityManager em = new JPAUtil().getEntityManager();
	        try {
	        	return new PlanoDAO(em).busca(Integer.parseInt(value));
	        } catch(Exception e) {
	        	e.printStackTrace();
	        } finally {
	        	em.close();
	        }
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object plano) {
		if (plano instanceof Plano) {
			return String.valueOf(((Plano)plano).getId());
		}
		return "";
	}
}