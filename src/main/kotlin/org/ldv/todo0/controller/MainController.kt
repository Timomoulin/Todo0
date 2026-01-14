package org.ldv.todo0.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
@Controller
class MainController {

    @GetMapping("/", "/todoapp/")
    fun index(): String {
        return "pagesVisiteur/home"
    }

    @GetMapping("/todoapp/login")
    fun login(@RequestParam error: Boolean?, model: Model): String {
        // Ajoute un attribut "error" au modèle si la requête contient une erreur
        model.addAttribute("error", error == true)
        return "pagesVisiteur/login"
    }
@PreAuthorize("isAuthenticated()")
    @GetMapping("/todoapp/profil")
    fun profil(authentication: Authentication): String {
        val roles = authentication.authorities.map { it.authority }
        if ( roles.contains("ROLE_ADMIN") ) {
            return "redirect:/todoapp/admin/profil"
        }

            return "pagesVisiteur/profil"

    }
@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/todoapp/admin/profil")
    fun adminProfil(): String {
        return "pagesAdmin/profilAdmin"
    }

}