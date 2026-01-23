package org.ldv.todo0.controller

import org.ldv.todo0.model.dao.CategorieDao
import org.ldv.todo0.model.entity.Categorie
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

/**
 * Contrôleur de gestion des catégories dans l'espace administrateur.
 *
 * Ce contrôleur permet aux administrateurs de gérer les catégories
 * utilisées pour classer les Todos.
 *
 * Fonctionnalités :
 * - affichage de la liste des catégories
 * - création d'une nouvelle catégorie
 * - modification d'une catégorie existante
 * - suppression d'une catégorie
 *
 * Il s'agit d'une implémentation classique du pattern CRUD
 * (Create, Read, Update, Delete) avec Spring MVC et Thymeleaf.
 *
 * @param categorieDao DAO permettant l'accès aux données des catégories
 */
@Controller
class AdminCategorieController(
    private val categorieDao: CategorieDao
) {

    /**
     * Affiche la liste de toutes les catégories.
     *
     * @param model objet permettant de transmettre les catégories à la vue
     * @return le template affichant la liste des catégories
     */
    @GetMapping("/todoapp/admin/categories")
    fun index(model: Model): String {
        val categories = categorieDao.findAll()
        model.addAttribute("categories", categories)
        return "pagesAdmin/categorie/index"
    }

    /**
     * Affiche le formulaire de création d'une catégorie.
     *
     * @param model objet contenant une catégorie vide pour le data binding
     * @return le template du formulaire de création
     */
    @GetMapping("/todoapp/admin/categories/create")
    fun create(model: Model): String {
        val nouvelleCategorie = Categorie(
            nom = "",
            couleur = ""
        )
        model.addAttribute("categorie", nouvelleCategorie)
        return "pagesAdmin/categorie/create"
    }

    /**
     * Traite la soumission du formulaire de création d'une catégorie.
     *
     * @param categorie objet rempli automatiquement à partir du formulaire
     * @param redirectAttributes permet d'envoyer un message flash
     * @return redirection vers la liste des catégories
     */
    @PostMapping("/todoapp/admin/categories")
    fun store(
        @ModelAttribute categorie: Categorie,
        redirectAttributes: RedirectAttributes
    ): String {
        categorieDao.save(categorie)
        redirectAttributes.addFlashAttribute(
            "msg",
            "La catégorie '${categorie.nom}' a bien été créée."
        )
        return "redirect:/todoapp/admin/categories"
    }

    /**
     * Affiche le formulaire de modification d'une catégorie existante.
     *
     * @param id identifiant de la catégorie à modifier
     * @param model objet permettant de transmettre la catégorie à la vue
     * @return le template du formulaire d'édition
     */
    @GetMapping("/todoapp/admin/categories/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        model: Model
    ): String {
        val categorie = categorieDao.findById(id).orElseThrow()
        model.addAttribute("categorie", categorie)
        return "pagesAdmin/categorie/edit"
    }

    /**
     * Traite la soumission du formulaire de modification d'une catégorie.
     *
     * @param categorie catégorie modifiée
     * @param redirectAttributes permet d'envoyer un message flash
     * @return redirection vers la liste des catégories
     */
    @PostMapping("/todoapp/admin/categories/update")
    fun update(
        @ModelAttribute categorie: Categorie,
        redirectAttributes: RedirectAttributes
    ): String {
        categorieDao.save(categorie)
        redirectAttributes.addFlashAttribute(
            "msg",
            "La catégorie '${categorie.nom}' a bien été modifiée."
        )
        return "redirect:/todoapp/admin/categories"
    }

    /**
     * Supprime une catégorie.
     *
     * @param id identifiant de la catégorie à supprimer
     * @param redirectAttributes permet d'envoyer un message flash
     * @return redirection vers la liste des catégories
     */
    @PostMapping("/todoapp/admin/categories/delete")
    fun delete(
        @RequestParam id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        categorieDao.deleteById(id)
        redirectAttributes.addFlashAttribute(
            "msg",
            "La catégorie a bien été supprimée."
        )
        return "redirect:/todoapp/admin/categories"
    }
}
