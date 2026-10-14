package com.sio2.taxiparking.config;

import com.sio2.taxiparking.entity.*;
import com.sio2.taxiparking.enumeration.*;
import com.sio2.taxiparking.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;

@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner init(UtilisateurRepository ur, PlaceRepository pr, PasswordEncoder pe) {
		return args -> {
			if (!ur.existsByEmail("admintaxi@gmail.com")) {
				Utilisateur a = new Utilisateur();
				a.setNom("Administrateur Taxi");
				a.setEmail("admintaxi@gmail.com");
				a.setMotDePasse(pe.encode("123admin"));
				a.setRole(RoleUtilisateur.ADMIN);
				a.setActif(true);
				ur.save(a);
			}
			if (pr.count() == 0) {
				for (int i = 1; i <= 20; i++) {
					Place p = new Place();
					p.setNumero(String.format("P%02d", i));
					p.setZone(i <= 10 ? "A" : "B");
					p.setType(i % 10 == 0 ? TypePlace.PMR : TypePlace.STANDARD);
					p.setStatut(StatutPlace.LIBRE);
					pr.save(p);
				}
			}
		};
	}
}
