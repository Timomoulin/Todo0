package org.ldv.todo0.security

import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import java.io.IOException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Gestionnaire personnalisé des accès refusés (HTTP 403).
 *
 * Cette classe est déclenchée lorsqu'un utilisateur authentifié
 * tente d'accéder à une ressource pour laquelle il n'a pas
 * les autorisations nécessaires.
 *
 * Objectifs :
 * - tracer les tentatives d'accès interdites
 * - enregistrer les informations sensibles dans les logs d'audit
 * - rediriger l'utilisateur vers une page 403 personnalisée
 */
@Component
class AuditAccessDeniedHandler : AccessDeniedHandler {

    /** Logger dédié à l'audit de sécurité (audit.log) */
    private val auditLogger = LoggerFactory.getLogger("AUDIT")

    /** Format de date utilisé pour l'horodatage */
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    /**
     * Méthode appelée automatiquement par Spring Security
     * lorsqu'un accès est refusé (HTTP 403).
     *
     * @param request requête HTTP entrante
     * @param response réponse HTTP envoyée au client
     * @param accessDeniedException exception indiquant un refus d'accès
     */
    @Throws(IOException::class)
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {

        /** Horodatage de l'événement */
        val timestamp = LocalDateTime.now().format(dateFormatter)

        /** Adresse IP du client */
        val ip = request.remoteAddr

        /**
         * Nom de l'utilisateur si authentifié,
         * sinon "ANONYME"
         */
        val username = request.userPrincipal?.name ?: "ANONYME"

        /** URL que l'utilisateur a tenté d'accéder */
        val url = request.requestURI

        /**
         * Log d'audit de sécurité :
         * - date / heure
         * - utilisateur
         * - IP
         * - ressource demandée
         */
        auditLogger.warn(
            "[{}] ACCÈS REFUSÉ : Utilisateur {} depuis IP {} a tenté d'accéder à {}",
            timestamp,
            username,
            ip,
            url
        )

        /**
         * Redirection vers une page d'erreur 403 personnalisée
         */
        response.sendRedirect("/403")
    }
}
