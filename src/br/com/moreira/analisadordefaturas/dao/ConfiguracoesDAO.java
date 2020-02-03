package br.com.moreira.analisadordefaturas.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.moreira.analisadordefaturas.filtro.ConfiguracoesPersonalizadas;

public class ConfiguracoesDAO {
	EntityManager em;
	private final DAO<ConfiguracoesPersonalizadas> dao;

	public ConfiguracoesDAO(EntityManager em) {
		super();
		dao = new DAO<ConfiguracoesPersonalizadas>(em, ConfiguracoesPersonalizadas.class);
		this.em = em;
	}
	
	public void adiciona(ConfiguracoesPersonalizadas config) {
		this.dao.adiciona(config);
	}
	
	public void atualiza(ConfiguracoesPersonalizadas config) {
		this.dao.atualiza(config);
	}
	
	public void remove(ConfiguracoesPersonalizadas config) {
		this.dao.remove(config);
	}
	
	public ConfiguracoesPersonalizadas busca(Integer id) {
		return dao.busca(id);
	}
	
	public ConfiguracoesPersonalizadas buscaPorIdCliente(Integer id) {
		Query query = em.createQuery("from ConfiguracoesPersonalizadas where cliente.id = :id").setParameter("id", id);

		return (ConfiguracoesPersonalizadas) query.getSingleResult();
	}
}
