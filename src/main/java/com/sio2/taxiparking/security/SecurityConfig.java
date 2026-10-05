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

    /**
     * Spring Security cherche l'utilisateur
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
                 * =========================
                 * PAGES PUBLIQUES
                 * =========================
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
                 * =========================
                 * API REST
                 * =========================
                 */
                .requestMatchers("/api/**")
                .hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "OPERATOR",
                        "USER"
                )


                /*
                 * =========================
                 * CHAUFFEURS
                 * =========================
                 */
                .requestMatchers("/chauffeurs/**")
                .hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "OPERATOR",
                        "USER"
                )


                /*
                 * =========================
                 * VÉHICULES
                 * =========================
                 */
                .requestMatchers("/vehicules/**")
                .hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "OPERATOR",
                        "USER"
                )


                /*
                 * =========================
                 * CLIENTS
                 * =========================
                 */
                .requestMatchers("/clients/**")
                .hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "OPERATOR",
                        "USER"
                )


                /*
                 * =========================
                 * COURSES
                 * =========================
                 */
                .requestMatchers("/courses/**")
                .hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "OPERATOR",
                        "USER"
                )


                /*
                 * =========================
                 * ANCIENNE ROUTE DRIVERS
                 * =========================
                 * Conservée temporairement.
                 */
                .requestMatchers("/drivers/**")
                .hasAnyRole(
                        "ADMIN",
                        "MANAGER",
                        "OPERATOR",
                        "USER"
                )


                /*
                 * =========================
                 * ACCUEIL
                 * =========================
                 */
                .requestMatchers(
                        "/",
                        "/accueil"
                ).authenticated()


                /*
                 * =========================
                 * TOUT LE RESTE
                 * =========================
                 */
                .anyRequest()
                .authenticated()
            )


            /*
             * =========================
             * CONNEXION
             * =========================
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
             * =========================
             * DÉCONNEXION
             * =========================
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