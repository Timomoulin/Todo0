package org.ldv.todo0.controller

import org.ldv.todo0.model.dao.RoleDao
import org.ldv.todo0.model.dao.UtilisateurDao
import org.ldv.todo0.model.entity.Role
import org.ldv.todo0.model.entity.Utilisateur
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class MainController (
    val utilisateurDao: UtilisateurDao,
    val roleDao: RoleDao,
    val passwordEncoder: PasswordEncoder
) {

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
    fun profil(authentication: Authentication,model: Model): String {
        val roles = authentication.authorities.map { it.authority }
        if ( roles.contains("ROLE_ADMIN") ) {
            return "redirect:/todoapp/admin/profil"
        }
    val utilisateur = utilisateurDao.findByEmail(authentication.name)
            model.addAttribute("utilisateur", utilisateur)
            return "pagesUtilisateur/profil"

    }
@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/todoapp/admin/profil")
    fun adminProfil(): String {
        return "pagesAdmin/profilAdmin"
    }

    @GetMapping("todoapp/inscription")
    fun showForm(model: Model): String {

        val utilisateur = Utilisateur(
            nom = "",
            prenom = "",
            email = "",
            mdp = "",
        )
        model.addAttribute("utilisateur", utilisateur )
        return "pagesVisiteur/inscription"
    }

    @PostMapping("/todoapp/inscription",)
    fun submitForm(
        @ModelAttribute("utilisateur") utilisateurForm: Utilisateur,
        redirectAttributes: RedirectAttributes
    ): String {

        if (utilisateurDao.existsByEmail(utilisateurForm.email)) {
            redirectAttributes.addFlashAttribute(
                "error",
                "Un compte avec cet email existe déjà."
            )
            return "redirect:/todoapp/inscription"
        }

        // Vérifier mots de passe identiques
        if (utilisateurForm.mdp != utilisateurForm.confirmationMdp) {
            redirectAttributes.addFlashAttribute(
                "error",
                "Les mots de passe ne correspondent pas."
            )
            return "redirect:/todoapp/inscription"
        }
        val roleUtilisateur = roleDao.findByNomIgnoreCase("UTILISATEUR").orElseThrow { IllegalStateException("UTILISATEUR introuvable")}

val nouvelleUtilisateur= Utilisateur(
    nom=utilisateurForm.nom,
    prenom=utilisateurForm.prenom,
    email=utilisateurForm.email,
    mdp=passwordEncoder.encode(utilisateurForm.mdp)!!,
    role=roleUtilisateur
)

        utilisateurDao.save(nouvelleUtilisateur)

        redirectAttributes.addFlashAttribute(
            "msg",
            "Compte créé avec succès. Vous pouvez vous connecter."
        )

        return "redirect:/todoapp/login"
    }

}