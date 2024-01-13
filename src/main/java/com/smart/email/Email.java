package com.smart.email;

public class Email {
private String receipient;
private String msgBody;
private String subject;
private String attachment;
public Email(String receipient, String msgBody, String subject, String attachment) {
	super();
	this.receipient = receipient;
	this.msgBody = msgBody;
	this.subject = subject;
	this.attachment = attachment;
}
public Email() {
	super();
	// TODO Auto-generated constructor stub
}
@Override
public String toString() {
	return "Email [receipient=" + receipient + ", msgBody=" + msgBody + ", subject=" + subject + ", attachment="
			+ attachment + "]";
}
public String getReceipient() {
	return receipient;
}
public void setReceipient(String receipient) {
	this.receipient = receipient;
}
public String getMsgBody() {
	return msgBody;
}
public void setMsgBody(String msgBody) {
	this.msgBody = msgBody;
}
public String getSubject() {
	return subject;
}
public void setSubject(String subject) {
	this.subject = subject;
}
public String getAttachment() {
	return attachment;
}
public void setAttachment(String attachment) {
	this.attachment = attachment;
}
}
