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


@Configuration
@EnableMethodSecurity
class SecurityConfig (
    private val auditAccessDeniedHandler: AuditAccessDeniedHandler
){
    // Logger dédié à l'audit (fichier audit.log)
    private val auditLogger = LoggerFactory.getLogger("AUDIT")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .headers { it.frameOptions { frameOptions -> frameOptions.sameOrigin() } }
           // .csrf { it.disable() }
            //Restriction des endpoints en fonction du role
            .authorizeHttpRequests {
                it.requestMatchers( "/","/h2-console/**","/todoapp" ,"/500","/404", "/todoapp/inscription", "/todoapp/login", "/css/**", "/js/**", "/img/**", "/favicon.ico").permitAll()
                    // Autoriser l'accès pour les utilisateurs avec le rôle "ADMIN" à /admin/**
                    .requestMatchers("/todoapp/admin/**").hasRole("ADMIN")
                    // Autoriser l'accès pour les utilisateurs avec le rôle "UTILISATEUR" à /utilisateur/**
                    .requestMatchers("/todoapp/utilisateur/**").hasAnyRole("UTILISATEUR","ADMIN")
                    // Toutes les autres requêtes doivent être authentifiées
                    .anyRequest().authenticated()

            }
            // Configuration du formulaire de connexion
            .formLogin { form: FormLoginConfigurer<HttpSecurity?> ->
                form
                    .loginPage("/todoapp/login").defaultSuccessUrl("/todoapp/profil").failureUrl("/todoapp/login?error=true")
                    .loginProcessingUrl("/todoapp/login")
                    .permitAll()
            }

            // Configuration du mécanisme de déconnexion
            .logout { logout: LogoutConfigurer<HttpSecurity?> ->
                logout
                    .logoutUrl("/todoapp/logout")
                    .permitAll()
            }
            .exceptionHandling { exceptions ->
                exceptions.accessDeniedHandler(auditAccessDeniedHandler)
            }
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { request, response, authException ->
                    val ip = request.remoteAddr
                    val url = request.requestURI
                    auditLogger.warn("Accès non authentifié depuis IP {} sur {}", ip, url)
                    response.sendRedirect("/todoapp/login")
                }
            }

        return http.build()
    }

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}