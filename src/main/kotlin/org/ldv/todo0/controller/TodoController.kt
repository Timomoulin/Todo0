package org.ldv.todo0.controller

import org.ldv.todo0.model.dao.CategorieDao
import org.ldv.todo0.model.dao.TodoDao
import org.ldv.todo0.model.entity.Todo
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
class AdminTodoController(
    val todoDao: TodoDao,
    val categorieDao: CategorieDao
) {


    @GetMapping("/todoapp/admin/todos")
    fun index(model: Model): String {
        val todos = todoDao.findAll()
        model.addAttribute("todos", todos)
        return "pagesAdmin/todo/index"
    }


    @GetMapping("/todoapp/admin/todos/{id}")
    fun show(@PathVariable id: Long, model: Model): String {

        return "pagesAdmin/todo/show"
    }


    @GetMapping("/todoapp/admin/todos/create")
    fun create(model: Model): String {
        val nouveauTodo = Todo(
            titre = "",
            description = ""
        )
        val categories = categorieDao.findAll()
        model.addAttribute("categories", categories)
        model.addAttribute("todo", nouveauTodo)
        return "pagesAdmin/todo/create"
    }


    @PostMapping("/todoapp/admin/todos")
    fun store(
        @ModelAttribute todo: Todo,
        redirectAttributes: RedirectAttributes
    ): String {
        todoDao.save(todo)
        redirectAttributes.addFlashAttribute("msg", "Le todo a bien été créé")
        return "redirect:/todoapp/admin/todos"
    }


    @GetMapping("/todoapp/admin/todos/edit/{id}")
    fun edit(@PathVariable id: Long, model: Model): String {
        val todo = todoDao.findById(id).orElseThrow()
        val categories = categorieDao.findAll()
        model.addAttribute("categories", categories)
        model.addAttribute("todo", todo)
        return "pagesAdmin/todo/edit"
    }


    @PostMapping("/todoapp/admin/todos/update")
    fun update(
        @ModelAttribute todo: Todo,
        redirectAttributes: RedirectAttributes
    ): String {
        todoDao.save(todo)
        redirectAttributes.addFlashAttribute(
            "msg",
            "Le todo '${todo.titre}' a bien été modifié."
        )
        return "redirect:/todoapp/admin/todos"
    }


    @PostMapping("/todoapp/admin/todos/delete")
    fun delete(
        @RequestParam id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        todoDao.deleteById(id)
        redirectAttributes.addFlashAttribute("msg", "Le todo a bien été supprimé.")
        return "redirect:/todoapp/admin/todos"
    }
}
