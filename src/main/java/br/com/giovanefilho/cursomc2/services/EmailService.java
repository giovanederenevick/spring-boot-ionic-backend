package br.com.giovanefilho.cursomc2.services;

import org.springframework.mail.SimpleMailMessage;

import br.com.giovanefilho.cursomc2.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
