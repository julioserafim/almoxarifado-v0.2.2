package ufc.npi.clinicas.service.impl;



import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ufc.npi.clinicas.model.Token;
import ufc.npi.clinicas.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{
		@Autowired
		private Environment env;
	
		@Autowired
		private JavaMailSender mailSender;
	
		@Autowired
		private TemplateEngine templateEngine;
	
		private void enviarEmail(MimeMessage email) {
			this.mailSender.send(email);
		}
		private final Logger log = LoggerFactory.getLogger(this.getClass());
		
	
		@Override
		public void emailRecuperacaoSenha(Token token) {
			Runnable email = new Runnable() {
	
				@Override
				public void run() {
					final MimeMessage mimeMessage = mailSender.createMimeMessage();
					final MimeMessageHelper mensagem = new MimeMessageHelper(mimeMessage, "UTF-8");
	
					final Context contexto = new Context();
	
					contexto.setVariable("token", token.getToken());
					contexto.setVariable("link", env.getProperty("sistema.link"));
	
					final String conteudo = templateEngine.process("mail/recuperar-senha", contexto);
	
					try {
						mensagem.setTo(token.getUsuario().getEmail());
						mensagem.setFrom("naoresponda@clinicas.ufc.br");
						mensagem.setSubject("Clínicas - Recuperar Senha");
						mensagem.setText(conteudo, true);
					} catch (MessagingException e) {
						log.error(e.getMessage());
					}
	
					enviarEmail(mimeMessage);
				}
			};
	
			Thread thread = new Thread(email);
			thread.start();
		}
}
