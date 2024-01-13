package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entity.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping({ "/home", "/" })
	public String homePage(Model model) {

		model.addAttribute("title", "Home- Smart Contact Manager");
		return "index.html";

	}

	@GetMapping("/about")
	public String aboutPage(Model model) {

		model.addAttribute("title", "About- Smart Contact Manager");
		return "about.html";

	}

	@GetMapping("/signin")
	public String login(Model model) {

		model.addAttribute("title", "Login- Smart Contact Manager");
		return "loginPage.html";

	}

	@GetMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Signup- Smart Contact Manager");
		return "register.html";
	}

	@RequestMapping(path = "/do_register", method = RequestMethod.POST)
	public String processSignup(@Valid @ModelAttribute("user") User user,
			@RequestParam(value = "aggreement", defaultValue = "false") boolean aggreement, Model model,
			BindingResult result1, HttpSession session) {
		try {
			System.out.println("Aggreement " + aggreement);
			if (!aggreement) {
				System.out.println("You have not aggreed the terms and condition");
				throw new Exception("You have not aggreed the terms and condition");
			}

			if (result1.hasErrors()) {
				System.out.println("Error " + result1.toString());
				model.addAttribute("user", user);
				return "register";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImgurl("default.png");
			user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
			System.out.println(user);
			User result = this.userRepository.save(user);
			User newUser = new User();
			model.addAttribute("user", newUser);
			session.setAttribute("message", new Message("Successfully Registered !! ", "alert-success"));
			return "register.html";
			// model.addAttribute("user", result);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong!! " + e.getMessage(), "alert-danger"));
			return "register.html";
		}
	}
}
