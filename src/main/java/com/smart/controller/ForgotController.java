package com.smart.controller;

import java.lang.ProcessBuilder.Redirect;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.smart.dao.UserRepository;
import com.smart.email.Email;
import com.smart.email.EmailServiceImpl;
import com.smart.entity.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	EmailServiceImpl emailServiceImpl;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/forgot")
	public String openForgotForm() {
		return "forgot.html";
	}

	@PostMapping("/send-otp")
	public String sendOtpToEmail(@RequestParam("username") String username, HttpSession session, Model model) {
		Random rand = new Random();
		int nextInt = rand.nextInt(99999);
		System.out.println("Random Value " + nextInt);
		System.out.println(username);

		User user = this.userRepository.getUserByUserName(username);
		if (user != null) {
			if (user.getEmail().equalsIgnoreCase(username)) {
				session.setAttribute("otp", nextInt);
				Email email = new Email();
				email.setReceipient(username);
				email.setMsgBody("OTP IS " + nextInt);
				email.setSubject("Reset Password");
				email.setAttachment("");

				boolean sent = this.emailServiceImpl.sendSimpleMain(email);
				if (sent) {
					System.out.println("Email Sent");

					session.setAttribute("message", new Message("OTP Sent.Please Check your Email", "alert-success"));
					session.setAttribute("otp", nextInt);
					session.setAttribute("useremail", user.getEmail());
					session.setMaxInactiveInterval(60);
					return "verify.html";
				}

			} else {
				session.setAttribute("message", new Message("Please Enter your Correct Email!.....", "alert-danger"));
				return "redirect:/forgot";

			}
		}
		session.setAttribute("message", new Message("Not able to send email.....", "alert-danger"));
		return "redirect:/forgot";

	}

	@PostMapping("/verify-otp")
	public String verifyOTP(@RequestParam("verify-otp") int otp, Model model, HttpSession session) {
		int sentOTP = (int) session.getAttribute("otp");

		if (sentOTP == otp) {
			System.out.println("OTP verified");
					return "changePassword.html";
		} else {
			System.out.println("OTP is invalid");
			session.setAttribute("message", new Message("Enter correct OTP ", "alert-danger"));
			return "verify.html";
		}
	}
	
	@PostMapping("/change-pass")
	public String changePassword(@RequestParam("oldpass") String oldpass,@RequestParam("newpass") String newpass, Model model,HttpSession session) {
		/*
		 * HttpServletRequest request = ((ServletRequestAttributes)
		 * RequestContextHolder.getRequestAttributes()) .getRequest(); session =
		 * request.getSession();
		 */
		
		String email= (String) session.getAttribute("useremail");
		User user=this.userRepository.getUserByUserName(email);
		System.out.println(user);
		if(user!=null) {
			if (this.passwordEncoder.matches(oldpass, user.getPassword())) {
				user.setPassword(this.passwordEncoder.encode(newpass));
				this.userRepository.save(user);
				System.out.println("Password Saved Successfully");
				session.setAttribute("message", new Message("Password Changed Successfully ", "alert-sucess"));
				return "redirect:/signin";
				
			}
		}
		
		return "redirect:/signin";
		

	}
	
	

}
