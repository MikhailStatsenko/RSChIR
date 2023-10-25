package com.authservice;

import com.authservice.model.Role;
import com.authservice.model.UserCredential;
import com.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.HashSet;

@SpringBootApplication
@EnableDiscoveryClient
@RequiredArgsConstructor
public class AuthServiceApplication implements CommandLineRunner {
	private final AuthService authService;
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Override
	public void run(String... args) {
		UserCredential user = new UserCredential(1L, "user",
				"user@gmail.com", "user", new HashSet<>());
		user.addRole(Role.SELLER);
		authService.updateUser(user);

		UserCredential seller = new UserCredential(2L, "seller",
				"seller@gmail.com", "seller", new HashSet<>());
		seller.addRole(Role.SELLER);
		authService.updateUser(seller);

		UserCredential admin = new UserCredential(3L, "admin",
				"admin@gmail.com", "admin", new HashSet<>());
		admin.addRole(Role.ADMINISTRATOR);
		authService.updateUser(admin);
	}
}


