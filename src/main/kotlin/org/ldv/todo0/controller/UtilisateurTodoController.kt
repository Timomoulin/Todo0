package org.ldv.todo0.controller

import org.ldv.todo0.model.dao.CategorieDao
import org.ldv.todo0.model.dao.TodoDao
import org.ldv.todo0.model.entity.Todo
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UtilisateurTodoController(
    private val todoDao: TodoDao,
    private val categorieDao: CategorieDao
) {

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
}
