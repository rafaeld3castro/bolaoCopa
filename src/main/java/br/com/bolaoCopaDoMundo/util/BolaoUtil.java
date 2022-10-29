package br.com.bolaoCopaDoMundo.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Component;

import br.com.bolaoCopaDoMundo.domain.Participante;
import br.com.bolaoCopaDoMundo.exception.BolaoCopaDoMundoRuntimeException;

@Component
public class BolaoUtil {
	
	public boolean validarCPF(String cpf) 
	{ 
		cpf = removerMascara(cpf);
		
		int d1, d2, digito1, digito2, resto, digitoCPF;  
		String  nDigResult;  
		
		d1 = d2 = digito1 = digito2 = resto = 0;  
		
		for (int nCount = 1; nCount < cpf.length() -1; nCount++)  
		{  
			digitoCPF = Integer.valueOf (cpf.substring(nCount -1, nCount)).intValue();  
			//multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.  
			d1 = d1 + ( 11 - nCount ) * digitoCPF;  
			//para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.  
			d2 = d2 + ( 12 - nCount ) * digitoCPF;  
		};
		
		//Primeiro resto da divisão por 11.  
		resto = (d1 % 11);
		
		//Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.  
		if (resto < 2)  
			digito1 = 0;  
		else  
			digito1 = 11 - resto;  
		
		d2 += 2 * digito1;  
		
		//Segundo resto da divisão por 11.  
		resto = (d2 % 11);  
		
		//Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.  
		if (resto < 2)  
			digito2 = 0;  
		else  
			digito2 = 11 - resto;  
		
		//Digito verificador do CPF que está sendo validado.  
		String nDigVerific = cpf.substring (cpf.length()-2, cpf.length());  
		
		//Concatenando o primeiro resto com o segundo.  
		nDigResult = String.valueOf(digito1) + String.valueOf(digito2);  
		
		//comparar o digito verificador do cpf com o primeiro resto + o segundo resto.  
		return nDigVerific.equals(nDigResult);  
	}
	
	public String removerMascara(String string) 
	{
		string = string.replace(".", "");
		string = string.replace("-", "");
		string = string.replace("/", "");
		string = string.replace("(", "");
		string = string.replace(")", "");
		return string;
	}
	
	public boolean validarEmail(String email)
	{  
		return email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	}
	
	public String criptografarSenha(String senha) throws Exception 
	{
	   MessageDigest md = MessageDigest.getInstance("MD5"); 
	   BigInteger hash = new BigInteger(1, md.digest(senha.getBytes())); 
	   String s = hash.toString(16); 
	   if (s.length() %2 != 0) {
	      s = "0" + s; 
	   }
	   
	   return s; 
	}
	
	@SuppressWarnings("deprecation")
	public void enviaEmailSimples(Participante destinatario, String assunto, String mensagem) throws EmailException {  
		try{
	  /*      SimpleEmail email = new SimpleEmail();  
	        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
	       // email.setHostName("webmail.tce.ce.gov.br");
	        email.addTo(destinatario.getEmail(), destinatario.getNome()); //destinatário  
	        email.setFrom("bolaocopadomundofc@gmail.com", "Bolão da Copa"); // remetente  
	        email.setSubject(assunto); // assunto do e-mail  
	        email.setMsg(mensagem); //conteudo do e-mail  
	        email.setAuthentication("bolaocopadomundofc", "bolao@07162534");  
	        email.setSmtpPort(587); 
	       // email.setSmtpPort(25);
	        email.setSSL(true);  
	        email.setTLS(true); 
	        email.send();   */
	        
	        SimpleEmail email = new SimpleEmail(); 
	        email.setDebug(true);
	        email.setHostName("smtp.gmail.com");
	        email.setSmtpPort(465);
	        // email.setSSLOnConnect(true);
	        email.setSSL(true); 
	        email.setAuthentication("bolaocopadomundofc", "bolao@07162534"); 
	       
	        email.addTo(destinatario.getEmail(), destinatario.getNome());
	        email.setFrom("bolaocopadomundofc@gmail.com", "Bolão da Copa"); // remetente  
	        email.setSubject(assunto); // assunto do e-mail  
	        email.setMsg(mensagem); //conteudo do e-mail 
	        email.send();
		} catch (EmailException e) {  
			throw new BolaoCopaDoMundoRuntimeException("Falha ao enviar e-mail de confirmação! Verifique se seu e-mail foi digitado corretamente, e se o erro persistir entre em contato com a administração.");  
  
        }
	} 
	
	@SuppressWarnings("deprecation")
	public void enviarEmailAdmin( String assunto, String mensagem) throws EmailException {  
		try{
	      /*  SimpleEmail email = new SimpleEmail();  
	        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
	       // email.setHostName("webmail.tce.ce.gov.br");
	        email.addTo("bolaocopadomundofc@gmail.com", "Administrador"); //destinatário  
	        email.setFrom("bolaocopadomundofc@gmail.com", "Bolão da Copa"); // remetente  
	        email.setSubject(assunto); // assunto do e-mail  
	        email.setMsg(mensagem); //conteudo do e-mail  
	        email.setAuthentication("bolaocopadomundofc", "bolao@07162534");  
	        
	       // email.setSmtpPort(587); 
//	        email.setSmtpPort(465);
//	        email.setStartTLSRequired(true);
	        email.setSmtpPort(465); 
	       // email.setSmtpPort(25);
	        email.setSSL(true);  
	       
	        email.setTLS(true); 
	        email.send();   */
	        
			SimpleEmail email = new SimpleEmail(); 
	        email.setDebug(true);
	        email.setHostName("smtp.gmail.com");
	        email.setSmtpPort(465);
	        // email.setSSLOnConnect(true);
	        email.setSSL(true); 
	        email.setAuthentication("bolaocopadomundofc", "bolao@07162534"); 
	       
	        email.addTo("bolaocopadomundofc@gmail.com", "Administrador"); //destinatário  
	        email.setFrom("bolaocopadomundofc@gmail.com", "Bolão da Copa"); // remetente  
	        email.setSubject(assunto); // assunto do e-mail  
	        email.setMsg(mensagem); //conteudo do e-mail 
	        email.send();
	        
		} catch (EmailException e) { 
			
			throw new BolaoCopaDoMundoRuntimeException("Falha ao enviar e-mail de confirmação! Verifique se seu e-mail foi digitado corretamente, e se o erro persistir entre em contato com a administração.");  
  
        }
	} 
	
	@SuppressWarnings("deprecation")
	public void enviarEmailGeral( String assunto, String mensagem,List<String> emails) throws EmailException {  
		try{
	     /*   SimpleEmail email = new SimpleEmail();  
	        email.setHostName("smtp.gmail.com"); // o servidor SMTP para envio do e-mail
	       // email.setHostName("webmail.tce.ce.gov.br");
	       // email.addTo("bolaocopadomundofc@gmail.com", "Administrador"); //destinatário 
	        for (String emailParticipante : emails) {
	        	email.addBcc(emailParticipante);
			}
	        email.setFrom("bolaocopadomundofc@gmail.com", "Bolão da Copa"); // remetente  
	        email.setSubject(assunto); // assunto do e-mail  
	        email.setMsg(mensagem); //conteudo do e-mail  
	        email.setAuthentication("bolaocopadomundofc", "bolao@07162534");  
	        email.setSmtpPort(587); 
	        
	       // email.setSmtpPort(25);
	        email.setSSL(true);  
	        email.setTLS(true); 
	        email.send();  */
	        
	        SimpleEmail email = new SimpleEmail(); 
	        email.setDebug(true);
	        email.setHostName("smtp.gmail.com");
	        email.setSmtpPort(465);
	        // email.setSSLOnConnect(true);
	        email.setSSL(true); 
	        email.setAuthentication("bolaocopadomundofc", "bolao@07162534"); 
	       
	        for (String emailParticipante : emails) {
	        	email.addBcc(emailParticipante);
			}
	        email.setFrom("bolaocopadomundofc@gmail.com", "Bolão da Copa"); // remetente  
	        email.setSubject(assunto); // assunto do e-mail  
	        email.setMsg(mensagem); //conteudo do e-mail 
	        email.send();
		} catch (EmailException e) {  
			throw new BolaoCopaDoMundoRuntimeException("Falha ao enviar e-mail de confirmação! Verifique se seu e-mail foi digitado corretamente, e se o erro persistir entre em contato com a administração.");  
  
        }
	} 
	
	public void sendAttachEmail(List<String> to, String subject, String body, String attach)
	{
		Properties p = getProps();
        Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getInstance(p, auth);
		MimeMessage msg = new MimeMessage(session);
		MimeBodyPart mbp = new MimeBodyPart();
		try {
			// "de" e "para"!!
			msg.setFrom(new InternetAddress("bolaocopadomundofc@gmail.com"));
			InternetAddress [] emails = new InternetAddress[to.size()];
			int i = 0;
			for (Iterator iterator = to.iterator(); iterator.hasNext();) {
				String toAddr = (String) iterator.next();
				emails[i] = new InternetAddress(toAddr);
				i++;
			}
			msg.setRecipients(Message.RecipientType.TO, emails);
			msg.setSentDate(new Date());
			msg.setSubject(subject);
			msg.setText(body);
			//enviando anexo
			DataSource fds = new FileDataSource(attach);
			mbp.setDisposition(Part.ATTACHMENT);
			mbp.setDataHandler(new DataHandler(fds));
			mbp.setFileName(fds.getName());
            Multipart mp = new MimeMultipart();   
			mp.addBodyPart(mbp);
			msg.setContent(mp);
			// enviando mensagem
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		System.out.println("Enviado com sucesso!");
	}
	private static Properties getProps() {
		Properties p = new Properties();		
		p.put("mail.transport.protocol", "smtp");
		p.put("mail.smtp.starttls.enable","true");
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.auth", "true");
        return p;
	}
}
class SMTPAuthenticator extends javax.mail.Authenticator {
	public PasswordAuthentication getPasswordAuthentication() {
	return new PasswordAuthentication ("bolaocopadomundofc", "bolao@07162534");
	}
	 
}
