package com.sio2.taxiparking.security;

import com.sio2.taxiparking.entity.Utilisateur;
import com.sio2.taxiparking.repository.UtilisateurRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Spring Security cherche l'utilisateur par son email
     * directement dans la base de données.
     */
    @Bean
    UserDetailsService users(UtilisateurRepository repository) {

        return email -> {

            Utilisateur utilisateur = repository
                    .findByEmail(
                            email.trim().toLowerCase()
                    )
                    .filter(Utilisateur::isActif)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(
                                    "Utilisateur introuvable"
                            )
                    );

            return User
                    .withUsername(
                            utilisateur.getEmail()
                    )
                    .password(
                            utilisateur.getMotDePasse()
                    )
                    .roles(
                            utilisateur
                                    .getRole()
                                    .name()
                    )
                    .build();
        };
    }

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http

            .authorizeHttpRequests(auth -> auth

                /*
                 * Pages publiques
                 */
                .requestMatchers(
                        "/login",
                        "/inscription",
                        "/css/**",
                        "/img/**",
                        "/images/**",
                        "/js/**",
                        "/favicon.ico"
                ).permitAll()

                /*
                 * API
                 */
                .requestMatchers("/api/**")
                .hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "OPERATOR",
                        "USER"
                )

                /*
                 * Gestion des chauffeurs
                 *
                 * Pour le moment USER peut également
                 * accéder à la page pendant le développement.
                 */
                .requestMatchers("/chauffeurs/**")
                .hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "OPERATOR",
                        "USER"
                )

                /*
                 * Ancienne route conservée temporairement
                 * pour éviter les liens cassés.
                 */
                .requestMatchers("/drivers/**")
                .hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "OPERATOR",
                        "USER"
                )

                /*
                 * Accueil
                 */
                .requestMatchers(
                        "/",
                        "/accueil"
                ).authenticated()

                /*
                 * Tout le reste nécessite une connexion.
                 */
                .anyRequest()
                .authenticated()
            )

            /*
             * Connexion
             */
            .formLogin(form -> form

                .loginPage("/login")

                .defaultSuccessUrl(
                        "/",
                        true
                )

                .permitAll()
            )

            /*
             * Déconnexion
             */
            .logout(logout -> logout

                .logoutSuccessUrl(
                        "/login?logout=true"
                )

                .permitAll()
            );

        return http.build();
    }
}