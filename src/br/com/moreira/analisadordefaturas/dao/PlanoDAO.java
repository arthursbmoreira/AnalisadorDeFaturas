package br.com.moreira.analisadordefaturas.dao;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.moreira.analisadordefaturas.modelo.Cliente;
import br.com.moreira.analisadordefaturas.modelo.Plano;

public class PlanoDAO {
	private final EntityManager em;
	private final DAO<Plano> dao;

	public PlanoDAO(EntityManager em) {
		super();
		dao = new DAO<Plano>(em, Plano.class);
		this.em = em;
	}
	
	public void adiciona(Plano plano) {
		this.dao.adiciona(plano);
	}
	
	public void atualiza(Plano plano) {
		this.dao.atualiza(plano);
	}
	
	public void remove(Plano plano) {
		this.dao.remove(plano);
	}
	
	public Plano busca(Integer id) {
		return dao.busca(id);
	}
	
	public List<Plano> lista(Cliente cliente) {
		return em.createQuery("from Plano p where p.cliente = :cliente", Plano.class)
				.setParameter("cliente", cliente)
				.getResultList();
	}
}