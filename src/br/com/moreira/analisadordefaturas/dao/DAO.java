package br.com.moreira.analisadordefaturas.dao;

import java.util.List;

import javax.persistence.EntityManager;

public class DAO<T> {
	private final EntityManager em;
	private final Class<T> classe;
	
	public DAO(EntityManager em, Class<T> classe) {
		super();
		this.em = em;
		this.classe = classe;
	}
	
	public void adiciona(T t) {
		this.em.persist(t);
	}
	
	public void atualiza(T t) {
		em.merge(t);
	}
	
	public void remove(T t) {
		this.em.remove(t);
	}
	
	public T busca(Integer id) {
		return em.find(classe, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> lista() {
		return em.createQuery("select o from " + classe.getName() + " o").getResultList();
	}
}
