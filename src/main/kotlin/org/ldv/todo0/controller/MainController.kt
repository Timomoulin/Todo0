package org.ldv.todo0.controller

import jakarta.validation.Valid
import org.ldv.todo0.model.dao.RoleDao
import org.ldv.todo0.model.dao.UtilisateurDao
import org.ldv.todo0.model.entity.Role
import org.ldv.todo0.model.entity.Utilisateur
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
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
    val logger = LoggerFactory.getLogger(AdminTodoController::class.java)

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
        @Valid @ModelAttribute("utilisateur") utilisateurForm: Utilisateur,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {

        // Vérification email unique
        if (utilisateurDao.existsByEmail(utilisateurForm.email)) {
            bindingResult.rejectValue(
                "email",
                "error.email",
                "Un compte avec cet email existe déjà"
            )
        }

        // Vérification confirmation mot de passe
        if (utilisateurForm.mdp != utilisateurForm.confirmationMdp) {
            bindingResult.rejectValue(
                "confirmationMdp",
                "error.confirmationMdp",
                "Les mots de passe ne correspondent pas"
            )
        }

        // Validation mot de passe fort
        val passwordPattern =
            Regex("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}\$")

        if (!passwordPattern.matches(utilisateurForm.mdp)) {
            bindingResult.rejectValue(
                "mdp",
                "error.mdp",
                "Le mot de passe doit contenir au moins 8 caractères, une majuscule, un chiffre et un caractère spécial"
            )
        }

        // Si erreurs → retourne formulaire
        if (bindingResult.hasErrors()) {
            return "pagesVisiteur/inscription"
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