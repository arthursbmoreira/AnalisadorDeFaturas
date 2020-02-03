package br.com.moreira.analisadordefaturas.teste;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

public class TesteJPA {
	
	public static void main(String args[]) {
		
		
//		System.out.println(GeradorDeSenha.gerarNovaSenha());
		
//		Cliente cliente = new Cliente();
//		cliente.seteMail("arthurmoreirabsi@gmail.com");
//		cliente.setSenhaTemporaria("123");
//		Mail.enviarEmailRecuperacaoDeSenha(cliente);
//		System.out.println("Email enviado");
		
//		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//		Calendar cal = Calendar.getInstance();
//		System.out.println(dateFormat.format(cal.getTime()));
		
//		cal.get(Calendar.DAY_OF_MONTH);
		
//		new TesteJPA().imprime("Teste");
		
		Calendar c = Calendar.getInstance();
		System.out.println(c.getTime());
	}
	
    public void detectaImpressoras() {  
  
        try {  
  
            DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;  
            PrintService[] ps = PrintServiceLookup.lookupPrintServices(df, null);  
            for (PrintService p: ps) {  
  
                System.out.println("Impressora encontrada: " + p.getName());  
  
                if (p.getName().contains("Text") || p.getName().contains("Generic"))  {  
  
                    System.out.println("Impressora Selecionada: " + p.getName());  
//                    impressora = p;  
                    break;  
  
                }  
  
            }  
  
        } catch (Exception e) {  
  
            e.printStackTrace();  
  
        }  
  
    }  
  
    public synchronized void imprime(String texto) {  
		InputStream stream = new ByteArrayInputStream(texto.getBytes());
		
		PrintService impressora = PrintServiceLookup.lookupDefaultPrintService();

		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		Doc doc = new SimpleDoc(stream, flavor, null);
		
//			PrintRequestAttributeSet printerAttributes = new HashPrintRequestAttributeSet();
//			printerAttributes.add(new JobName("Impressao", null));
//			printerAttributes.add(OrientationRequested.PORTRAIT);
//			printerAttributes.add(MediaSizeName.ISO_A4);
//			printerAttributes.add(new Copies(1));   

		DocPrintJob dpj = impressora.createPrintJob();
//			dpj.print(doc, (PrintRequestAttributeSet) printerAttributes);
		try {
			dpj.print(doc, null);

		} catch (PrintException e) {
			e.printStackTrace();
		}
    }

}
