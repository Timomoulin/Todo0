package org.ldv.todo0.service

import org.ldv.todo0.security.AuditAccessDeniedHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import java.time.format.DateTimeFormatter

/**
 * Configuration principale de la sécurité de l'application.
 *
 * Cette classe définit :
 * - la gestion de l'authentification (login / logout)
 * - la gestion des autorisations (rôles)
 * - l'encodage des mots de passe
 * - le comportement en cas d'accès non autorisé
 *
 * Elle remplace l'ancienne configuration basée sur WebSecurityConfigurerAdapter
 * (supprimée dans les versions récentes de Spring Security).
 */
@Configuration
@EnableMethodSecurity
class SecurityConfig(
    /** Gestionnaire personnalisé pour auditer les accès refusés */
    private val auditAccessDeniedHandler: AuditAccessDeniedHandler
) {

    /** Logger dédié à l'audit de sécurité (audit.log) */
    private val auditLogger = LoggerFactory.getLogger("AUDIT")

    /** Formateur de date pour les logs (actuellement non utilisé directement) */
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    /**
     * Bean Spring chargé de l'encodage des mots de passe.
     *
     * BCrypt est recommandé car :
     * - il est robuste
     * - il intègre un "salt"
     * - il est lent par conception (protection contre le brute force)
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    /**
     * Chaîne de filtres de sécurité Spring.
     *
     * Cette méthode définit toutes les règles de sécurité HTTP :
     * - quelles URL sont publiques
     * - quelles URL nécessitent une authentification
     * - quels rôles sont requis
     * - comportement du formulaire de login
     * - gestion des erreurs de sécurité
     */
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http
            /**
             * Autorise l'affichage de la console H2 dans un navigateur.
             * Nécessaire uniquement en environnement de développement.
             */
            .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } }

            // Désactivation CSRF possible pour les tests (à éviter en production)
            // .csrf { it.disable() }

            /**
             * Définition des règles d'autorisation selon les URL.
             */
            .authorizeHttpRequests {

                it.requestMatchers(
                    "/",
                    "/h2-console/**",
                    "/todoapp",
                    "/500",
                    "/404",
                    "/todoapp/inscription",
                    "/todoapp/login",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/favicon.ico"
                ).permitAll()

                    /** Accès réservé aux administrateurs */
                    .requestMatchers("/todoapp/admin/**").hasRole("ADMIN")

                    /** Accès réservé aux utilisateurs authentifiés */
                    .requestMatchers("/todoapp/utilisateur/**")
                    .hasAnyRole("UTILISATEUR", "ADMIN")

                    /** Toutes les autres requêtes nécessitent une authentification */
                    .anyRequest().authenticated()
            }

            /**
             * Configuration du formulaire de connexion.
             */
            .formLogin { form: FormLoginConfigurer<HttpSecurity?> ->
                form
                    .loginPage("/todoapp/login")
                    .loginProcessingUrl("/todoapp/login")
                    .defaultSuccessUrl("/todoapp/profil")
                    .failureUrl("/todoapp/login?error=true")
                    .permitAll()
            }

            /**
             * Configuration du mécanisme de déconnexion.
             */
            .logout { logout: LogoutConfigurer<HttpSecurity?> ->
                logout
                    .logoutUrl("/todoapp/logout")
                    .permitAll()
            }

            /**
             * Gestion des accès refusés (utilisateur authentifié mais sans autorisation).
             */
            .exceptionHandling { exceptions ->
                exceptions.accessDeniedHandler(auditAccessDeniedHandler)
            }

            /**
             * Gestion des accès non authentifiés (utilisateur non connecté).
             * Un log d'audit est généré avant la redirection vers la page de login.
             */
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { request, response, _ ->
                    val ip = request.remoteAddr
                    val url = request.requestURI
                    auditLogger.warn(
                        "Accès non authentifié depuis IP {} sur {}",
                        ip,
                        url
                    )
                    response.sendRedirect("/todoapp/login")
                }
            }

        return http.build()
    }

    /**
     * Bean permettant à Spring Security de gérer l'authentification.
     *
     * Il est utilisé notamment par :
     * - Spring Security
     * - les services d'authentification
     */
    @Bean
    fun authenticationManager(
        config: AuthenticationConfiguration
    ): AuthenticationManager {
        return config.authenticationManager
    }
}
