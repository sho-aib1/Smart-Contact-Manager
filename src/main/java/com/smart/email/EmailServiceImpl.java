package com.smart.email;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;

@Component
public class EmailServiceImpl implements EmailService {

	static String SENDER_USER = "mdsho048@gmail.com";
	static String SENDER_PASSWORD = "pusw rznq cvri hmjh";

	@Override
	public boolean sendSimpleMain(Email details) {
		boolean status = false;
		try {
			Properties properties = System.getProperties();
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.auth", "true");

			Session session = Session.getInstance(properties, new Authenticator() {

				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					// TODO Auto-generated method stub
					return new PasswordAuthentication(SENDER_USER, SENDER_PASSWORD);
				}

			});
			
			
			MimeMessage msgMailMessage = new MimeMessage(session);

			msgMailMessage.setFrom(new InternetAddress(SENDER_USER));
			msgMailMessage.setRecipients(RecipientType.TO, InternetAddress.parse("mshoaib03233@gmail.com"));
			msgMailMessage.setContent("<h1> " + details.getMsgBody() + "</h1> ", "text/html; charset=utf-8");

			msgMailMessage.setSubject(details.getSubject());

			Transport.send(msgMailMessage);
			status = true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return status;

	}

	@Override
	public String sendMailWithAttachment(Email details) {
		// TODO Auto-generated method stub
		return null;
	}

}
