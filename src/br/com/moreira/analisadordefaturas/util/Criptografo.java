package br.com.moreira.analisadordefaturas.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.com.moreira.analisadordefaturas.modelo.Cliente;

public class Criptografo {
	
	public static String criptografar(String texto) {
		String resultado = "";
		
		MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance("MD5");
	
			byte[] valorMD5 = mDigest.digest(texto.getBytes("UTF-8"));
	
			StringBuffer sb = new StringBuffer();
			for (byte b : valorMD5) {
				sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
			}
			resultado = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return resultado;
	}

	public static String gerarToken(Cliente cliente) {
		return criptografar(cliente.getNome()+cliente.geteMail()+cliente.getCpfCnpj());
	}
}
