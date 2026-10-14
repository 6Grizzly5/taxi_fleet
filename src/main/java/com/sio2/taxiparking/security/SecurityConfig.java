package com.sio2.taxiparking.security;

import com.sio2.taxiparking.entity.Utilisateur;
import com.sio2.taxiparking.repository.UtilisateurRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    UserDetailsService users(UtilisateurRepository repository) {
        return email -> {
            Utilisateur user = repository.findByEmail(email.trim().toLowerCase())
                    .filter(Utilisateur::isActif)
                    .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable ou désactivé"));
            return User.withUsername(user.getEmail())
                    .password(user.getMotDePasse())
                    .roles(user.getRole().name())
                    .build();
        };
    }

    /** REST API: stateless Basic Auth for easy Postman use. CSRF is disabled only for this stateless API chain. */
    @Bean
    @Order(1)
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/statistics/**", "/api/clients/**", "/api/rides/*/assign", "/api/rides/*/cancel").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/drivers/**", "/api/vehicles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/rides/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/rides").hasAnyRole("ADMIN", "CLIENT")
                        .requestMatchers(HttpMethod.POST, "/api/rides/*/start", "/api/rides/*/complete").hasRole("CHAUFFEUR")
                        .anyRequest().hasAnyRole("ADMIN", "CLIENT", "CHAUFFEUR"))
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /** Thymeleaf web interface: form login and CSRF protection remain enabled. */
    @Bean
    @Order(2)
    SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/inscription", "/inscription/chauffeur", "/css/**", "/img/**", "/error", "/erreur/acces-refuse").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/client/**").hasRole("CLIENT")
                        .requestMatchers("/chauffeur/**").hasRole("CHAUFFEUR")
                        .requestMatchers("/clients/**", "/chauffeurs/**", "/vehicules/**", "/courses/**", "/parking/**").hasRole("ADMIN")
                        .requestMatchers("/", "/accueil").authenticated()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
                .exceptionHandling(errors -> errors.accessDeniedPage("/erreur/acces-refuse"))
                .logout(logout -> logout.logoutSuccessUrl("/login?logout=true").permitAll());
        return http.build();
    }
}
