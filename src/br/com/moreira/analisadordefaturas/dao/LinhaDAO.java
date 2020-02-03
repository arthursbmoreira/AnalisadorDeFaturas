package br.com.moreira.analisadordefaturas.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.moreira.analisadordefaturas.modelo.Linha;

public class LinhaDAO {
	private final DAO<Linha> dao;
	private final EntityManager em;

	public LinhaDAO(EntityManager em) {
		super();
		dao = new DAO<Linha>(em, Linha.class);
		this.em = em;
	}
	
	public void adiciona(Linha linha) {
		this.dao.adiciona(linha);
	}
	
	public void atualiza(Linha linha) {
		this.dao.atualiza(linha);
	}
	
	public void remove(Linha linha) {
		this.dao.remove(linha);
	}
	
	public Linha busca(Integer id) {
		return dao.busca(id);
	}
	
	public Linha buscaPorNumero(String numero) {
		return em.createQuery("select l from Linha l where l.numero = :numero", Linha.class)
				.setParameter("numero", numero)
				.getSingleResult();
	}
	
	public ArrayList<Linha> lista() {
		return (ArrayList<Linha>) dao.lista();
	}
	
	public List<Linha> listaPorId(int id) {
		return this.em.createQuery("select l from Linha l where l.cliente.id = :id", Linha.class).setParameter("id", id).getResultList();
	}
}
