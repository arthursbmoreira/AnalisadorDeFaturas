package br.com.moreira.analisadordefaturas.modelo;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
	private static final String eMail = "analisadordefaturas@analisadordefaturas.com";
	private static final String senha = "UsuarioSistema*#1000";
	
	public static void enviarEMail(final String from, final String senha, String assunto, String mensagem, String... to) {
		Properties props = System.getProperties();

		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, senha);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from)); // Remetente

			StringBuilder destinatarios = new StringBuilder();
			for (String destinatario : to) {
				destinatarios.append(destinatario + ", ");
			}
			destinatarios.trimToSize();
			destinatarios.deleteCharAt(destinatarios.lastIndexOf(","));
			Address[] toUser = InternetAddress.parse(destinatarios.toString());// Destinatário(s)
			
			message.setRecipients(Message.RecipientType.TO, toUser);
			
			message.setSubject(assunto);// Assunto
			
			message.setContent(mensagem, "text/html");
			
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void enviarEmailContato(String assunto, String mensagem) {
		enviarEMail(eMail, senha, assunto, mensagem , eMail);
	}
	
	public static void enviarEmailConfirmacao(Cliente cliente) {
		String mensagem = "Olá,"
				+ "<br>"
				+ "<br>"
				+ "Para confirmar seu cadastro no Analisador de Faturas, clique no botão abaixo."
				+ "<br>"
				+ "<br>"
				+ "<br>"
				+ "<a href=http://www.analisadordefaturas.com/?token=" + cliente.getToken()
				+ " style='width:300px;text-decoration:none;line-height:40px;display:inline-block;font-size:18px;border:0;background-color:#08c;color:#ffffff;border-radius:4px;text-align:center' "
				+ " target='_blank'>Confirmar Cadastro</a>"
				+ "<br>"
				+ "<br>"
				+ "<br>"
				+ "Atenciosamente, "
				+ "<br>"
				+ "<br>"
				+ "Equipe Analisador de Faturas.";
		
		enviarEMail(eMail, senha, "Confirmação de Cadastro", mensagem , cliente.geteMail());
	}
	
	public static void enviarEmailRecuperacaoDeSenha(Cliente cliente) {
		String mensagem = "Olá,"
				+ "<br>"
				+ "<br>"
				+ "Uma nova senha foi solicitada para sua conta. Sua nova senha é:"
				+ "<br>"
				+ "<br>"
				+ "<b>" + cliente.getSenhaTemporaria() + "</b>"
				+ "<br>"
				+ "<br>"
				+ "Por motivo de segurança a nova senha não da acesso ao sistema e precisa ser alterada."
				+ "Clique no botão abaixo para alterar sua senha."
				+ "<br>"
				+ "<br>"
				+ "<br>"
				+ "<a href=http://www.analisadordefaturas.com/recuperacao-de-senha.xhtml?mail=" + cliente.geteMail()
				+ " style='width:300px;text-decoration:none;line-height:40px;display:inline-block;font-size:18px;border:0;background-color:#08c;color:#ffffff;border-radius:4px;text-align:center' "
				+ " target='_blank'>Alterar Senha</a>"
				+ "<br>"
				+ "<br>"
				+ "<br>"
				+ "Atenciosamente, "
				+ "<br>"
				+ "<br>"
				+ "Equipe Analisador de Faturas.";
		
		enviarEMail(eMail, senha, "Recuperação de Senha", mensagem , cliente.geteMail());
	}
}
