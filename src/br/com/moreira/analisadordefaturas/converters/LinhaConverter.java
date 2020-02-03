package br.com.moreira.analisadordefaturas.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;

import br.com.moreira.analisadordefaturas.dao.LinhaDAO;
import br.com.moreira.analisadordefaturas.modelo.Linha;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

@FacesConverter(forClass=Linha.class)
public class LinhaConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value != null && !value.equals("")) {
			EntityManager em = new JPAUtil().getEntityManager();
	        try {
	        	Linha l = new LinhaDAO(em).busca(Integer.parseInt(value));
	        	return l;
	        } catch(Exception e) {
	        	e.printStackTrace();
	        } finally {
	        	em.close();
	        }
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object linha) {
		if (linha instanceof Linha) {
			return ((Linha)linha).toString();
		}
		return "";
	}
}
