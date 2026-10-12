package com.sio2.taxiparking.security;

import com.sio2.taxiparking.entity.Utilisateur;
import com.sio2.taxiparking.enumeration.RoleUtilisateur;
import com.sio2.taxiparking.repository.UtilisateurRepository;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService users(UtilisateurRepository r) {
		return email -> {
			Utilisateur u = r.findByEmail(email.trim().toLowerCase())
					.filter(Utilisateur::isActif)
					.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
			return User.withUsername(u.getEmail())
					.password(u.getMotDePasse())
					.roles(u.getRole().name())
					.build();
		};
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity h) throws Exception {
		h.authorizeHttpRequests(a -> a
				.requestMatchers("/login", "/inscription", "/inscription/chauffeur", "/css/**", "/img/**", "/error", "/erreur/acces-refuse").permitAll()
				.requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
				.requestMatchers("/client/**").hasRole("CLIENT")
				.requestMatchers("/chauffeur/**").hasRole("CHAUFFEUR")
				.requestMatchers("/api/**").hasAnyRole("ADMIN", "CLIENT", "CHAUFFEUR")
				.requestMatchers("/clients/**", "/chauffeurs/**", "/vehicules/**", "/courses/**", "/parking/**").hasRole("ADMIN")
				.requestMatchers("/", "/accueil").authenticated()
				.anyRequest().authenticated())
				.formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
				.exceptionHandling(e -> e.accessDeniedPage("/erreur/acces-refuse"))
				.logout(l -> l.logoutSuccessUrl("/login?logout=true").permitAll());
		return h.build();
	}
}
