package com.fruitstore;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fruitstore.domain.User;
import com.fruitstore.domain.security.Role;
import com.fruitstore.domain.security.UserRole;
import com.fruitstore.service.UserService;

@SpringBootApplication
public class FruitStoreApplication implements CommandLineRunner {
	
	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(FruitStoreApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		User user1 = new User();
		user1.setFirstName("Asdf");
		user1.setLastName("Qwer");
		user1.setUsername("zx");
		user1.setPassword("wy");
		user1.setEmail("asdf@qwe.com");
		
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
	}

}
