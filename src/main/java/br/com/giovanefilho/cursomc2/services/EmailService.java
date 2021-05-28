package br.com.giovanefilho.cursomc2.services;

import javax.mail.internet.MimeMessage;

import br.com.giovanefilho.cursomc2.domain.Cliente;
import org.springframework.mail.SimpleMailMessage;

import br.com.giovanefilho.cursomc2.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	void sendHtmlEmail(MimeMessage msg);

    void sendNewPasswordEmail(Cliente cliente, String newPass);
}
