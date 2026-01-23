package org.ldv.todo0.security

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AbstractAuthenticationEvent
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Listener Spring Security permettant d'intercepter
 * les événements d'authentification (succès / échec).
 *
 * Cette classe est utilisée pour :
 * - auditer les connexions réussies
 * - auditer les tentatives de connexion échouées
 * - tracer l'utilisateur, l'heure et l'adresse IP
 *
 * Elle s'appuie sur le mécanisme d'événements de Spring.
 */
@Component
class AuthEventsListener : ApplicationListener<AbstractAuthenticationEvent> {

    /** Logger dédié à l'audit de sécurité (audit.log) */
    private val auditLogger = LoggerFactory.getLogger("AUDIT")

    /** Format de date utilisé dans les logs */
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    /**
     * Méthode appelée automatiquement par Spring
     * à chaque événement d'authentification.
     *
     * @param event événement Spring Security déclenché
     */
    override fun onApplicationEvent(event: AbstractAuthenticationEvent) {

        /** Informations d'authentification (utilisateur, rôles, etc.) */
        val auth: Authentication = event.authentication

        /**
         * Récupération de la requête HTTP courante afin d'obtenir l'adresse IP.
         * RequestContextHolder permet d'accéder au contexte web sans
         * dépendre directement d'un contrôleur.
         */
        val request =
            (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request

        /** Adresse IP du client */
        val ip = request?.remoteAddr ?: "IP inconnue"

        /** Horodatage de l'événement */
        val timestamp = LocalDateTime.now().format(dateFormatter)

        /**
         * Traitement selon le type d'événement reçu.
         */
        when (event) {

            /** Connexion réussie */
            is AuthenticationSuccessEvent -> {
                auditLogger.info(
                    "[{}] LOGIN OK : {} depuis IP {}",
                    timestamp,
                    auth.name,
                    ip
                )
            }

            /** Connexion échouée */
            is AbstractAuthenticationFailureEvent -> {
                auditLogger.warn(
                    "[{}] LOGIN ÉCHOUÉ : {} depuis IP {}",
                    timestamp,
                    auth.name,
                    ip
                )
            }
        }
    }
}
