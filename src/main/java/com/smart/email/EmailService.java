package com.smart.email;

public interface EmailService {
	
	boolean sendSimpleMain(Email details);
	
	String sendMailWithAttachment(Email details);

}
