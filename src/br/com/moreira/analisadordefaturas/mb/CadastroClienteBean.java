package br.com.moreira.analisadordefaturas.mb;

import java.util.Calendar;
import java.util.InputMismatchException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.primefaces.context.RequestContext;

import br.com.moreira.analisadordefaturas.dao.ClienteDAO;
import br.com.moreira.analisadordefaturas.modelo.Cliente;
import br.com.moreira.analisadordefaturas.modelo.Mail;
import br.com.moreira.analisadordefaturas.util.Criptografo;
import br.com.moreira.analisadordefaturas.util.JPAUtil;

@ManagedBean
@ViewScoped
public class CadastroClienteBean {
	
	private Cliente cliente = new Cliente();
	private String confirmaSenha;
	private String confirmaEmail;
     
	public void cadastrarCliente() {
		try {
			EntityManager em = new JPAUtil().getEntityManager();
			em.getTransaction().begin();
			
			ClienteDAO clienteDAO = new ClienteDAO(em);
			validarDadosCliente(clienteDAO);
	        
			cliente.setClienteAdimplente(false);
			cliente.setClienteAtivo(false);
			
			Calendar cal = Calendar.getInstance();
			
			cliente.setDataCriacao(cal.getTime());
			cliente.setDiaVencimento(cal.get(Calendar.DAY_OF_MONTH));
			cliente.setToken(Criptografo.gerarToken(cliente));
	        clienteDAO.adiciona(cliente);
	        
	        em.getTransaction().commit();
	        em.close();
	        
	        Mail.enviarEmailConfirmacao(cliente);
	        
	        cliente = new Cliente();
	        
	        RequestContext rc = RequestContext.getCurrentInstance();
		    rc.execute("PF('cadastro').hide()");
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!" ,"Cadastro realizado com sucesso. Acesse seu email para confirmação."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
		}
	}
	
	private void validarDadosCliente(ClienteDAO dao) throws Exception {
		String CpfCnpj = cliente.getCpfCnpj().replace(".", "").replace("/", "").replace("-", "");
		if(!isCNPJ(CpfCnpj) && !isCPF(CpfCnpj)) {
			throw new Exception("CPF/CNPJ inválido.");
		} else {
			cliente.setCpfCnpj(CpfCnpj);
		}
		if(!cliente.geteMail().trim().equals(this.confirmaEmail)) {
			throw new Exception("Os emails digitados não são iguais.");
		}
		if(cliente.getSenha().trim().length() < 5) {
			throw new Exception("A senha deve conter no mínimo 5 caracteres.");
		}
		if(!cliente.getSenha().trim().equals(this.confirmaSenha)) {
			throw new Exception("As senhas digitadas não são iguais.");
		} else {
			cliente.setSenha(Criptografo.criptografar(cliente.getSenha()));
		}
		if(dao.buscaPorCpfCnpj(cliente) != null)
			throw new Exception("CPF/CNPJ já cadastrado.");
		if(dao.buscaPorEmail(cliente) != null)
			throw new Exception("Email ja cadastrado.");
	}
	
	private boolean isCPF(String CPF) {
		if (CPF.equals("00000000000") || CPF.equals("11111111111")
				|| CPF.equals("22222222222") || CPF.equals("33333333333")
				|| CPF.equals("44444444444") || CPF.equals("55555555555")
				|| CPF.equals("66666666666") || CPF.equals("77777777777")
				|| CPF.equals("88888888888") || CPF.equals("99999999999")
				|| (CPF.length() != 11))
			return (false);
		char dig10, dig11;
		int sm, i, r, num, peso;
		try {
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}
			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48);

			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}
			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	private boolean isCNPJ(String CNPJ) {
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111")
				|| CNPJ.equals("22222222222222")
				|| CNPJ.equals("33333333333333")
				|| CNPJ.equals("44444444444444")
				|| CNPJ.equals("55555555555555")
				|| CNPJ.equals("66666666666666")
				|| CNPJ.equals("77777777777777")
				|| CNPJ.equals("88888888888888")
				|| CNPJ.equals("99999999999999") || (CNPJ.length() != 14))
			return (false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		try {
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {

				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}
			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}

	public Cliente getCliente() {
		return cliente;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public String getConfirmaEmail() {
		return confirmaEmail;
	}

	public void setConfirmaEmail(String confirmaEmail) {
		this.confirmaEmail = confirmaEmail;
	}
	
}
