package com.PFE;
import com.PFE.Entity.CamionEntity;
import com.PFE.Entity.CompteEntity;
import com.PFE.Entity.ManagerEntity;
import com.PFE.Entity.Vehicule;
import com.PFE.Service.CompteService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class PfeApplication {
	public static void main(String[] args) {
		SpringApplication.run(PfeApplication.class, args);
	}
}
