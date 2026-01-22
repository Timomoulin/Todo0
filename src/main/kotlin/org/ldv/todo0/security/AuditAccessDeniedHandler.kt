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

@Component
class AuditAccessDeniedHandler : AccessDeniedHandler {

    private val auditLogger = LoggerFactory.getLogger("AUDIT")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Throws(IOException::class)
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        val timestamp = LocalDateTime.now().format(dateFormatter)

        // IP du client
        val ip = request.remoteAddr

        // Nom d'utilisateur si disponible
        val username = request.userPrincipal?.name ?: "ANONYME"

        // URL demandée
        val url = request.requestURI

        auditLogger.warn(
            "[{}] ACCÈS REFUSÉ : Utilisateur {} depuis IP {} a tenté d'accéder à {}",
            timestamp, username, ip, url
        )

        // Redirection classique vers page 403
        response.sendRedirect("/403")
    }
}