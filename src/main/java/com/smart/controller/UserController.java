package com.smart.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entity.Contact;
import com.smart.entity.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public ContactRepository contactRepository;

	@Autowired
	public BCryptPasswordEncoder passwordEncoder;

	@ModelAttribute
	public void showCommonData(Model model, Principal principal) {

		String name = principal.getName();
		System.out.println("USERNAME " + name);
		User user = userRepository.getUserByUserName(name);
		System.out.println(user);

		model.addAttribute("user", user);
	}

	/**
	 * Show User Dashboard
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/index")
	public String dashboard(Model model) {

		model.addAttribute("User DashBoard", "Home- Smart Contact Manager");
		return "normal/userDashboard.html";
	}

	@GetMapping("/add-contact")
	public String addContactform(Model model) {
		model.addAttribute("Add Contact", "Home- Smart Contact Manager");
		return "normal/add_contact_form.html";
	}

	@GetMapping("/setting")
	public String openSetting(Model model, Principal principal) {
		model.addAttribute("Setting", "Home- Smart Contact Manager");
		return "normal/change_password.html";

	}

	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldpassword") String oldpass,
			@RequestParam("newpassword") String newpass, Model model, Principal principal, HttpSession session) {
		System.out.println("Old Password " + oldpass);
		System.out.println("New Password " + newpass);

		User user = this.userRepository.getUserByUserName(principal.getName());
		System.out.println("Encrypt old Password " + user.getPassword());

		if (this.passwordEncoder.matches(oldpass, user.getPassword())) {
			// change Password
			if (!oldpass.equalsIgnoreCase(newpass)) {
				user.setPassword(this.passwordEncoder.encode(newpass));
				this.userRepository.save(user);
				session.setAttribute("message", new Message("Password Changed Successfully ", "alert-success"));
			} else {
				System.out.println("New should should not be matches with old password");
				session.setAttribute("message",
						new Message("New should should not be matches with old password", "alert-danger"));
				return "redirect:/user/setting";
			}

		} else {
			// show error message
			session.setAttribute("message", new Message("Please Enter Correct Old Password ", "alert-danger"));

			return "redirect:/user/setting";
		}

		return "redirect:/user/index";

	}

	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact, Principal principal,
			@RequestParam("profileName") MultipartFile file, Model model, HttpSession session) {
		try {
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);

			if (file.isEmpty()) {
				System.out.println("img is empty");
				contact.setImg("contact.png");

			} else {
				contact.setImg(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/image").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("img is uploaded");
			}
			contact.setUser(user);
			user.getContact().add(contact);

			this.userRepository.save(user);

			System.out.println("Data " + contact);

			System.out.println("Added to Database");

			session.setAttribute("message", new Message("Successfully Registered !! ", "alert-success"));

		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
			session.setAttribute("message", new Message("Something went wrong!! " + e.getMessage(), "alert-danger"));
		}
		return "normal/add_contact_form.html";
	}

	@GetMapping("/show-contacts/{page}")
	public String viewContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		model.addAttribute("Show Contacts", "Home- Smart Contact Manager");

		String name = principal.getName();

		User user = userRepository.getUserByUserName(name);

		Pageable pageable = PageRequest.of(page, 3);
		Page<Contact> list = contactRepository.findContactByUser(user.getId(), pageable);
		list.forEach(s -> System.out.println(s.getName()));

		model.addAttribute("contact", list);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", list.getTotalPages());
		return "normal/show_contacts.html";
	}

	@GetMapping("/user-info")
	public String viewUserInfo(Model model, Principal principal) {
		model.addAttribute("My Profile", "Home- Smart Contact Manager");

		User user = this.userRepository.getUserByUserName(principal.getName());

		model.addAttribute("user", user);

		return "normal/user_profile.html";

	}

	@GetMapping("/contact/{cid}")
	public String viewContactDetails(@PathVariable("cid") Integer cid, Model model, Principal principal) {
		System.out.println("Contact ID " + cid);
		model.addAttribute("Contact Details", "Home- Smart Contact Manager");

		Optional<Contact> contactOptional = contactRepository.findById(cid);
		Contact contact = contactOptional.get();

		String name = principal.getName();

		User user = userRepository.getUserByUserName(name);
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			System.out.println(contact.toString());
		}

		return "normal/contact_details.html";

	}

	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, Principal principal, HttpSession session)
			throws IOException {
		System.out.println("Contact ID " + cid);
		model.addAttribute("Contact Details", "Home- Smart Contact Manager");

		Optional<Contact> contactOptional = contactRepository.findById(cid);
		Contact contact = contactOptional.get();

		String name = principal.getName();

		User user = userRepository.getUserByUserName(name);
		if (user.getId() == contact.getUser().getId()) {
			contact.setUser(null);

			if (!contact.getImg().equalsIgnoreCase("contact.png")) {
				File file = new ClassPathResource("static/image/" + contact.getImg()).getFile();
				System.out.println(file.getName());
				file.delete();
			}

			user.getContact().remove(contact);
			this.userRepository.save(user);
			// this.contactRepository.delete(contact);

			session.setAttribute("message", new Message("Contact Deleted !! ", "alert-success"));

		}

		return "redirect:/user/show-contacts/0";
	}

	@GetMapping("/update/{cid}")
	public String updateContactForm(@PathVariable("cid") Integer cid, Model model, Principal principal,
			HttpSession session) throws IOException {

		System.out.println("Contact ID " + cid);
		model.addAttribute("Contact Details", "Home- Smart Contact Manager");

		Optional<Contact> contactOptional = contactRepository.findById(cid);
		Contact contact = contactOptional.get();

		String name = principal.getName();

		User user = userRepository.getUserByUserName(name);

		model.addAttribute("contact", contact);

		return "normal/update_contact.html";
	}

	@PostMapping("/process-update/{cid}")
	public String updateContactHandler(@ModelAttribute Contact contact, @PathVariable("cid") Integer cid,
			@RequestParam("profileName") MultipartFile file, Model model, HttpSession session, Principal principal) {

		Contact oldContact = this.contactRepository.findById(cid).get();

		String name = principal.getName();
		User user = this.userRepository.getUserByUserName(name);
		System.out.println("Contact Name " + contact.getName());
		System.out.println("Contact ID " + contact.getCid());
		System.out.println(cid);
		try {

			if (!oldContact.getImg().equalsIgnoreCase("contact.png")) {
				File oldFile = new ClassPathResource("/static/image/" + oldContact.getImg()).getFile();
				oldFile.delete();
			}

			if (file.isEmpty()) {
				System.out.println("Image is Empty");
				contact.setImg("contact.png");
			} else {
				contact.setImg(file.getOriginalFilename());
				File saveFile;

				saveFile = new ClassPathResource("/static/image").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath(), File.separator, file.getOriginalFilename());
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("image updated");

			}
			contact.setUser(user);
			this.contactRepository.save(contact);
			System.out.println("contact updated");
			session.setAttribute("message", new Message("Contact Updated.. !! ", "alert-success"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		return "redirect:/user/contact/" + contact.getCid();

	}

}
