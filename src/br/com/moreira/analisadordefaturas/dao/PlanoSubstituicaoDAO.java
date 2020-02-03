package br.com.moreira.analisadordefaturas.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.moreira.analisadordefaturas.modelo.Cliente;
import br.com.moreira.analisadordefaturas.modelo.PlanoSubstituicao;

public class PlanoSubstituicaoDAO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final EntityManager em;
	private final DAO<PlanoSubstituicao> dao;

	public PlanoSubstituicaoDAO(EntityManager em) {
		super();
		dao = new DAO<PlanoSubstituicao>(em, PlanoSubstituicao.class);
		this.em = em;
	}
	
	public void adiciona(PlanoSubstituicao planoSubstituicao) {
		this.dao.adiciona(planoSubstituicao);
	}
	
	public void atualiza(PlanoSubstituicao planoSubstituicao) {
		this.dao.atualiza(planoSubstituicao);
	}
	
	public void remove(PlanoSubstituicao planoSubstituicao) {
		this.dao.remove(planoSubstituicao);
	}
	
	public PlanoSubstituicao busca(Integer id) {
		return dao.busca(id);
	}
	
	public List<PlanoSubstituicao> lista(Cliente cliente) {
		return em.createQuery("from PlanoSubstituicao p where p.cliente = :cliente", PlanoSubstituicao.class)
				.setParameter("cliente", cliente)
				.getResultList();
	}
}
