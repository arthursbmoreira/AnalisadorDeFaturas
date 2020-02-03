package br.com.moreira.analisadordefaturas.mb;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.PersistenceException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.primefaces.event.FileUploadEvent;

import br.com.moreira.analisadordefaturas.analisadores.AnalisadorInternetVivo;
import br.com.moreira.analisadordefaturas.analisadores.AnalisadorMensagensVivo;
import br.com.moreira.analisadordefaturas.analisadores.AnalisadorTelefoniaVivo;
import br.com.moreira.analisadordefaturas.construtores.ConstrutorFaturasVivo;
import br.com.moreira.analisadordefaturas.exceptions.ArquivoInvalidoException;
import br.com.moreira.analisadordefaturas.exceptions.LinhaException;
import br.com.moreira.analisadordefaturas.exceptions.PlanoException;
import br.com.moreira.analisadordefaturas.modelo.Empresa;
import br.com.moreira.analisadordefaturas.modelo.Fatura;
import br.com.moreira.analisadordefaturas.servico.ConstrutorEmpresa;

@ManagedBean
@SessionScoped
public class LoaderBean {
	
	private String nomeArquivo;
	private InputStream faturaPDF;
	
	private String fatura;
	
	private List<Fatura> listaFaturas;
	private List<Fatura> listaFaturasNumerosNaoUtilizados;
	private List<Fatura> listaFaturasComErro;
	
	private Empresa empresa = new Empresa();
	
	@ManagedProperty(value="#{loginBean}")
	private LoginBean loginBean;
	
	public void upload(FileUploadEvent event) {
		try {
			this.nomeArquivo = "";
			this.faturaPDF = null;
			this.nomeArquivo = event.getFile().getFileName();
			this.faturaPDF = event.getFile().getInputstream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "O arquivo da fatura " + event.getFile().getFileName() + " foi carregado com sucesso.");
		FacesContext.getCurrentInstance().addMessage("", message);
	}
	
	public String analisarFatura() {
		try {
			analisarArquivoPDF();
			construir();
			
			return "empresa?faces-redirect=true";
		} catch (ArquivoInvalidoException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("", message);
			
		} catch (PlanoException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("", message);
			
		} catch (LinhaException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", e.getMessage());
			FacesContext.getCurrentInstance().addMessage("", message);
		}
		return null;
	}
	
	public void analisarArquivoPDF() {
		try {
			this.fatura = "";
			PDDocument pdDocument = PDDocument.load(faturaPDF);
			fatura = new PDFTextStripper().getText(pdDocument);
			pdDocument.close();
			
		} catch (IOException e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Nenhum arquivo selecionado.");
			FacesContext.getCurrentInstance().addMessage("", message);
		}
	}
	
	public void construir() {
		try {
			ConstrutorFaturasVivo construtor = new ConstrutorFaturasVivo(fatura, loginBean.getCliente(), loginBean.getConfig(),
					new AnalisadorTelefoniaVivo(), new AnalisadorMensagensVivo(), new AnalisadorInternetVivo());
			
			construtor.construirFaturas();
			
			listaFaturas = construtor.getListaFaturas();
			listaFaturasNumerosNaoUtilizados = construtor.getListaFaturasNumerosNaoUtilizados();
			listaFaturasComErro = construtor.getListaFaturasComErro();
			
			ConstrutorEmpresa construtorEmpresa = new ConstrutorEmpresa(listaFaturas, listaFaturasNumerosNaoUtilizados, fatura);
			empresa = construtorEmpresa.construir();
			
		} catch(ArquivoInvalidoException e) {
			throw new ArquivoInvalidoException();
		} catch(PlanoException e) {
			e.printStackTrace();
			throw new PlanoException(e.getMessage());
		} catch(PersistenceException e) {
			e.printStackTrace();
			throw new LinhaException("A fatura que você tentou analisar contém uma ou mais linhas que pertencem a outros usuários.");
		}
	}
	
	public boolean isFaturaCarregada() {
		return fatura != null;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public String getFatura() {
		return fatura;
	}
	
	public void setFatura(String fatura) {
		this.fatura = fatura;
	}

	public List<Fatura> getListaFaturas() {
		return listaFaturas;
	}

	public void setListaFaturas(List<Fatura> listaFaturas) {
		this.listaFaturas = listaFaturas;
	}

	public List<Fatura> getListaFaturasNumerosNaoUtilizados() {
		return listaFaturasNumerosNaoUtilizados;
	}

	public void setListaFaturasNumerosNaoUtilizados(List<Fatura> listaFaturasNumerosNaoUtilizados) {
		this.listaFaturasNumerosNaoUtilizados = listaFaturasNumerosNaoUtilizados;
	}
	
	public List<Fatura> getListaFaturasComErro() {
		return listaFaturasComErro;
	}

	public void setListaFaturasComErro(List<Fatura> listaFaturasComErro) {
		this.listaFaturasComErro = listaFaturasComErro;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	
	public LoginBean getLoginBean() {
		return loginBean;
	}
	
	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}
}
