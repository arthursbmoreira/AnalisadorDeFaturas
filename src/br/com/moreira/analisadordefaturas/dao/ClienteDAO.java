package br.com.moreira.analisadordefaturas.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.moreira.analisadordefaturas.modelo.Cliente;
import br.com.moreira.analisadordefaturas.util.Criptografo;

public class ClienteDAO {
	private final EntityManager em;
	private final DAO<Cliente> dao;

	public ClienteDAO(EntityManager em) {
		super();
		dao = new DAO<Cliente>(em, Cliente.class);
		this.em = em;
	}
	
	public void adiciona(Cliente cliente) {
		this.dao.adiciona(cliente);
	}
	
	public void atualiza(Cliente cliente) {
		this.dao.atualiza(cliente);
	}
	
	public void remove(Cliente cliente) {
		this.dao.remove(cliente);
	}
	
	public Cliente busca(Integer id) {
		return dao.busca(id);
	}
	
	public List<Cliente> lista() {
		return dao.lista();
	}
	
	public Cliente buscaPorCpfCnpj(Cliente cliente) {
		Query query = em.createQuery("from Cliente where cpfCnpj = :cpfCnpj")
						.setParameter("cpfCnpj", cliente.getCpfCnpj());

		try {
			cliente = (Cliente) query.getSingleResult();
		return cliente;
		} catch(Exception e) {
			return null;
		}
	}
	
	public Cliente buscaPorEmail(Cliente cliente) {
		Query query = em.createQuery("from Cliente where eMail = :eMail")
				.setParameter("eMail", cliente.geteMail());

		try {
			cliente = (Cliente) query.getSingleResult();
		return cliente;
		} catch(Exception e) {
			return null;
		}
	}
	
	public Cliente buscaPorToken(Cliente cliente) {
		Query query = em.createQuery("from Cliente where token = :token")
				.setParameter("token", cliente.getToken());

		try {
			cliente = (Cliente) query.getSingleResult();
		return cliente;
		} catch(Exception e) {
			return null;
		}
	}
	
	public Cliente buscaPorLoginSenha(Cliente cliente) {
		Query query = em.createQuery("from Cliente where eMail = :eMail and senha = :senha")
						.setParameter("eMail", cliente.geteMail())
						.setParameter("senha", Criptografo.criptografar(cliente.getSenha()));

		try {
			cliente = (Cliente) query.getSingleResult();
		return cliente;
		} catch(Exception e) {
			return null;
		}
	}
	
	public Cliente buscaPorLoginSenhaTemporaria(Cliente cliente) {
		Query query = em.createQuery("from Cliente where eMail = :eMail and senhaTemporaria = :senhaTemporaria")
						.setParameter("eMail", cliente.geteMail())
						.setParameter("senhaTemporaria", Criptografo.criptografar(cliente.getSenhaTemporaria()));

		try {
			cliente = (Cliente) query.getSingleResult();
		return cliente;
		} catch(Exception e) {
			return null;
		}
	}
}
