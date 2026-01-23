package org.ldv.todo0.controller

import jakarta.validation.Valid
import org.ldv.todo0.model.dao.RoleDao
import org.ldv.todo0.model.dao.UtilisateurDao
import org.ldv.todo0.model.entity.Utilisateur
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

/**
 * Contrôleur principal de l'application.
 *
 * Cette classe gère :
 * - l'accueil
 * - l'authentification (login)
 * - l'inscription des utilisateurs
 * - l'affichage du profil utilisateur et administrateur
 *
 * Elle illustre le fonctionnement du pattern MVC avec Spring Boot :
 * - Controller : gestion des requêtes HTTP
 * - Model : transmission des données vers la vue
 * - View : templates Thymeleaf
 */
@Controller
class MainController(
    private val utilisateurDao: UtilisateurDao,
    private val roleDao: RoleDao,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * Page d'accueil accessible à tous.
     *
     * @return le template de la page d'accueil
     */
    @GetMapping("/", "/todoapp/")
    fun index(): String {
        return "pagesVisiteur/home"
    }

    /**
     * Affiche la page de connexion.
     *
     * @param error indique si une erreur d'authentification est survenue
     * @param model permet de transmettre l'information à la vue
     * @return le template de la page de login
     */
    @GetMapping("/todoapp/login")
    fun login(
        @RequestParam error: Boolean?,
        model: Model
    ): String {
        model.addAttribute("error", error == true)
        return "pagesVisiteur/login"
    }

    /**
     * Affiche le profil de l'utilisateur connecté.
     *
     * - Redirige les administrateurs vers leur espace dédié
     * - Affiche le profil classique pour les utilisateurs standards
     *
     * @param authentication informations de l'utilisateur connecté
     * @param model permet de transmettre l'utilisateur à la vue
     * @return la vue de profil appropriée
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/todoapp/profil")
    fun profil(
        authentication: Authentication,
        model: Model
    ): String {

        val roles = authentication.authorities.map { it.authority }

        if (roles.contains("ROLE_ADMIN")) {
            return "redirect:/todoapp/admin/profil"
        }

        val utilisateur = utilisateurDao.findByEmail(authentication.name)
        model.addAttribute("utilisateur", utilisateur)

        return "pagesUtilisateur/profil"
    }

    /**
     * Profil réservé aux administrateurs.
     *
     * @return la page de profil administrateur
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/todoapp/admin/profil")
    fun adminProfil(): String {
        return "pagesAdmin/profilAdmin"
    }

    /**
     * Affiche le formulaire d'inscription.
     *
     * @param model contient un objet Utilisateur vide pour le data binding
     * @return le template du formulaire d'inscription
     */
    @GetMapping("/todoapp/inscription")
    fun showForm(model: Model): String {

        val utilisateur = Utilisateur(
            nom = "",
            prenom = "",
            email = "",
            mdp = ""
        )

        model.addAttribute("utilisateur", utilisateur)
        return "pagesVisiteur/inscription"
    }

    /**
     * Traite la soumission du formulaire d'inscription.
     *
     * Étapes :
     * - validation des champs
     * - vérification email unique
     * - validation du mot de passe
     * - encodage du mot de passe
     * - sauvegarde en base
     *
     * @param utilisateurForm données du formulaire
     * @param bindingResult contient les erreurs de validation
     * @param redirectAttributes permet d'envoyer un message flash
     * @return redirection vers la page de login ou retour au formulaire
     */
    @PostMapping("/todoapp/inscription")
    fun submitForm(
        @Valid @ModelAttribute("utilisateur") utilisateurForm: Utilisateur,
        bindingResult: BindingResult,
        redirectAttributes: RedirectAttributes
    ): String {

        // Vérification unicité de l'email
        if (utilisateurDao.existsByEmail(utilisateurForm.email)) {
            bindingResult.rejectValue(
                "email",
                "error.email",
                "Un compte avec cet email existe déjà"
            )
        }

        // Vérification de la confirmation du mot de passe
        if (utilisateurForm.mdp != utilisateurForm.confirmationMdp) {
            bindingResult.rejectValue(
                "confirmationMdp",
                "error.confirmationMdp",
                "Les mots de passe ne correspondent pas"
            )
        }

        // Vérification de la robustesse du mot de passe
        val passwordPattern = Regex(
            "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}\$"
        )

        if (!passwordPattern.matches(utilisateurForm.mdp)) {
            bindingResult.rejectValue(
                "mdp",
                "error.mdp",
                "Le mot de passe doit contenir au moins 8 caractères, une majuscule, un chiffre et un caractère spécial"
            )
        }

        // En cas d'erreurs → retour au formulaire
        if (bindingResult.hasErrors()) {
            return "pagesVisiteur/inscription"
        }

        // Attribution du rôle UTILISATEUR
        val roleUtilisateur = roleDao
            .findByNomIgnoreCase("UTILISATEUR")
            .orElseThrow { IllegalStateException("UTILISATEUR introuvable") }

        // Création de l'utilisateur final
        val nouvelUtilisateur = Utilisateur(
            nom = utilisateurForm.nom,
            prenom = utilisateurForm.prenom,
            email = utilisateurForm.email,
            mdp = passwordEncoder.encode(utilisateurForm.mdp)!!,
            role = roleUtilisateur
        )

        utilisateurDao.save(nouvelUtilisateur)

        redirectAttributes.addFlashAttribute(
            "msg",
            "Compte créé avec succès. Vous pouvez vous connecter."
        )

        return "redirect:/todoapp/login"
    }
}
