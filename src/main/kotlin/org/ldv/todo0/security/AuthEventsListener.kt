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

@Component
class AuthEventsListener : ApplicationListener<AbstractAuthenticationEvent> {

    // Logger dédié à l'audit (fichier audit.log)
    private val auditLogger = LoggerFactory.getLogger("AUDIT")

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    override fun onApplicationEvent(event: AbstractAuthenticationEvent) {
        val auth: Authentication = event.authentication

        // Récupération de l'adresse IP
        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
        val ip = request?.remoteAddr ?: "IP inconnue"

        val timestamp = LocalDateTime.now().format(dateFormatter)

        when (event) {
            is AuthenticationSuccessEvent -> {
                auditLogger.info("[{}] LOGIN OK : {} depuis IP {}", timestamp, auth.name, ip)
            }
            is AbstractAuthenticationFailureEvent -> {
                auditLogger.warn("[{}] LOGIN ÉCHOUÉ : {} depuis IP {}", timestamp, auth.name, ip)
            }
        }
    }
}