package com.fruitstore.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fruitstore.domain.Product;
import com.fruitstore.domain.User;
import com.fruitstore.domain.security.PasswordResetToken;
import com.fruitstore.domain.security.Role;
import com.fruitstore.domain.security.UserRole;
import com.fruitstore.service.ProductService;
import com.fruitstore.service.UserService;
import com.fruitstore.service.impl.UserSecurityService;
import com.fruitstore.utility.MailConstructor;

@Controller
public class HomeController {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailConstructor mailConstructor;

	@Autowired
	private UserService userService;

//	@Autowired
//	private UserSecurityService userSecurityService;

	@Autowired
	private ProductService productService;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/index")
	public String indexx() {
		return "index";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "login";
	}
	
	@RequestMapping("/sig")
	public String sig() {
		return "sig";
	}


	/*
	 * @RequestMapping("/forgetPassword") public String forgetPassword(Model model)
	 * {
	 * 
	 * model.addAttribute("classActiveForgetPassword", true); return "login"; }
	 */

	@RequestMapping("/allProducts")
	public String allProducts(Model model) {
		List<Product> productList = productService.findAll();
		model.addAttribute("productList", productList);
		return "allProducts";
	}

	@RequestMapping("/productDetail")
	public String productDetails(
			@PathParam("id") Long id, Model model, Principal principal
			) {
		if(principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
		
		Product product = productService.findOne(id);
		model.addAttribute("product",product);
		
		List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		model.addAttribute("qtyList",qtyList);
		model.addAttribute("qty",1);
		return "productDetail";
		
	}
	
		
	@RequestMapping(value = "/sig",method = RequestMethod.POST)
	public String sigPost(
			@ModelAttribute("username") String username,
			@ModelAttribute("email") String userEmail, 
			@ModelAttribute("firstName") String firstName,
			@ModelAttribute("lastName") String lastName,
			@ModelAttribute("password") String password,
			Model model
			) throws Exception{
		model.addAttribute("username", username);
		model.addAttribute("email", userEmail);
		model.addAttribute("firstName",firstName);
		model.addAttribute("lastName",lastName);
		model.addAttribute("password",password);
		
		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);
			return "sig";
		}

		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);
			return "sig";
		}
		
		User user1 = new User();
		user1.setFirstName(firstName);
		user1.setLastName(lastName);
		user1.setUsername(username);
		user1.setPassword(password);
		user1.setEmail(userEmail);
		
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
		
		model.addAttribute("accountCreatedd","true");
		return "login";
	}
	

	@RequestMapping("/profile")
	public String profile(Model model, Principal principal) {
		String username = principal.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("classActiveEdit", true);
		return "profile";
	}
	
//	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
//	public String newUserPost(
//			HttpServletRequest request, @ModelAttribute("email") String userEmail,
//			@ModelAttribute("username") String username, Model model
//			) throws Exception {
//		model.addAttribute("classActiveNewAccount", true);
//		model.addAttribute("email", userEmail);
//		model.addAttribute("username", username);
//
//		if (userService.findByUsername(username) != null) {
//			model.addAttribute("usernameExists", true);
//
//			return "login";
//		}
//
//		if (userService.findByEmail(userEmail) != null) {
//			model.addAttribute("emailExists", true);
//	
//			return "login";
//		}
//
//		User user = new User();
//		user.setUsername(username);
//		user.setEmail(userEmail);
//
//		String password = SecurityUtility.randomPassword();
//
//		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
//		user.setPassword(encryptedPassword);
//
//		Role role = new Role();
//		role.setRoleId(1);
//		role.setName("ROLE_USER");
//		Set<UserRole> userRoles = new HashSet<>();
//		userRoles.add(new UserRole(user, role));
//		userService.createUser(user, userRoles);
//
//		String token = UUID.randomUUID().toString();
//		userService.createPasswordResetTokenForUser(user, token);
//
//		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//
//		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
//				password);
//
//		mailSender.send(email);
//
//		model.addAttribute("emailSent", "true");
//
//		return "login";
//	}
//
//	@RequestMapping("/newUser")
//	public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
//		PasswordResetToken passToken = userService.getPasswordResetToken(token);
//
//		if (passToken == null) {
//			String message = "Invalid Token.";
//			model.addAttribute("message", message);
//			return "redirect:/badRequest";
//		}
//
//		User user = passToken.getUser();
//		String username = user.getUsername();
//
//		UserDetails userDetails = userSecurityService.loadUserByUsername(username);
//
//		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
//				userDetails.getAuthorities());
//
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//
//		model.addAttribute("user", user);
//
//		model.addAttribute("classActiveEdit", true);
//		return "profile";
//	}
	
//	@RequestMapping("/forgetPassword")
//	public String forgetPassword(
//
//			HttpServletRequest request,
//
//			@ModelAttribute("email") String email, Model model) {
//
//		model.addAttribute("classActiveForgetPassword", true);
//
//		User user = userService.findByEmail(email);
//
//		if (user == null) {
//			model.addAttribute("emailNotExist", true);
//			return "login";
//		}
//
//		String password = SecurityUtility.randomPassword();
//
//		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
//		user.setPassword(encryptedPassword);
//
//		userService.save(user);
//
//		String token = UUID.randomUUID().toString();
//		userService.createPasswordResetTokenForUser(user, token);
//
//		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//
//		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user,
//				password);
//
//		mailSender.send(newEmail);
//
//		model.addAttribute("forgetPasswordEmailSent", "true");
//
//		return "login";
//	}

}
