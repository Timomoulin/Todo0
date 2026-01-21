package org.ldv.todo0.controller

import jakarta.validation.Valid
import org.ldv.todo0.model.dao.CategorieDao
import org.ldv.todo0.model.dao.TodoDao
import org.ldv.todo0.model.entity.Todo
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class UtilisateurTodoController(
    private val todoDao: TodoDao,
    private val categorieDao: CategorieDao
) {

    @PreAuthorize("hasAnyRole('UTILISATEUR','ADMIN')")
    @GetMapping("/todoapp/utilisateur/todos")
    fun index(model: Model): String {
        val lesTodos =todoDao.findAll()
        model.addAttribute("todos", lesTodos)
        return "pagesUtilisateur/todos/index"
    }

    @GetMapping("/todoapp/utilisateur/todos/create")
    fun create(model: Model): String {
        val lesCategories = categorieDao.findAll()
        val nouveauTodo = Todo(titre = "", description = "")

        model.addAttribute("categories", lesCategories)
        model.addAttribute("todo", nouveauTodo)
        return "pagesUtilisateur/todos/create"
    }

    @PostMapping("/todoapp/utilisateur/todos")
    fun store(
        @ModelAttribute @Valid todo: Todo,
        bindingResult: BindingResult,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {

        if (bindingResult.hasErrors()) {
            model.addAttribute("todo",todo)
            model.addAttribute("categories", categorieDao.findAll())
            return "pagesUtilisateur/todos/create"
        }
        val nouveauTodo= todoDao.save(todo)
        redirectAttributes.addFlashAttribute(
            "msg",
            "Le Todo ${todo.titre} a bien été créé."
        )
        return "redirect:/todoapp/utilisateur/todos"
    }
}
