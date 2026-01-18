package org.ldv.todo0.controller

import org.ldv.todo0.model.dao.CategorieDao
import org.ldv.todo0.model.entity.Categorie
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class AdminCategorieController(
    val categorieDao: CategorieDao
) {

    @GetMapping("/todoapp/admin/categories")
    fun index(model: Model): String {
        val categories = categorieDao.findAll()
        model.addAttribute("categories", categories)
        return "pagesAdmin/categorie/index"
    }

    @GetMapping("/todoapp/admin/categories/create")
    fun create(model: Model): String {
        val nouvelleCategorie = Categorie(
            nom = "",
            couleur = ""
        )
        model.addAttribute("categorie", nouvelleCategorie)
        return "pagesAdmin/categorie/create"
    }

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

    @GetMapping("/todoapp/admin/categories/edit/{id}")
    fun edit(
        @PathVariable id: Long,
        model: Model
    ): String {
        val categorie = categorieDao.findById(id).orElseThrow()
        model.addAttribute("categorie", categorie)
        return "pagesAdmin/categorie/edit"
    }

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
