package com.example.TryingJWT;

import com.example.TryingJWT.domain.Account;
import com.example.TryingJWT.domain.Role;
import com.example.TryingJWT.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class TryingJwtApplication {

	@Autowired
	private AccountService accountService;

	public static void main(String[] args) {
		SpringApplication.run(TryingJwtApplication.class, args);
	}

	@Bean
	CommandLineRunner run(){
		return args -> {
			accountService.saveRole(new Role(null, "ROLE_USER"));
			accountService.saveRole(new Role(null, "ROLE_MANAGER"));
			accountService.saveRole(new Role(null, "ROLE_ADMIN"));
			accountService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			accountService.saveAccount(new Account(null, "John Travolta", "john", "1234", new ArrayList<>()));
			accountService.saveAccount(new Account(null, "Will Smith", "will", "1234", new ArrayList<>()));
			accountService.saveAccount(new Account(null, "Jim Carry", "jim", "1234", new ArrayList<>()));
			accountService.saveAccount(new Account(null, "Arnold Schwarznenegerr", "arnold", "1234", new ArrayList<>()));

			accountService.addRoleToAccount("john", "ROLE_USER");
			accountService.addRoleToAccount("john", "ROLE_MANAGER");
			accountService.addRoleToAccount("will", "ROLE_MANAGER");
			accountService.addRoleToAccount("jim", "ROLE_ADMIN");
			accountService.addRoleToAccount("arnold", "ROLE_SUPER_ADMIN");
			accountService.addRoleToAccount("arnold", "ROLE_ADMIN");
			accountService.addRoleToAccount("arnold", "ROLE_USER");
		};
	}

}
