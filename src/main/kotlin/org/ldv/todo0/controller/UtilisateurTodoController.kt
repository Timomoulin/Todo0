package org.ldv.todo0.controller

import org.ldv.todo0.model.dao.TodoDao
import org.ldv.todo0.model.entity.Todo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UtilisateurTodoController(
    private val todoDao: TodoDao
) {
    @GetMapping("/todoapp/utilisateur/todos")
    fun index(): String {

        val lesTodos = todoDao.findAll()
        return "pagesUtilisateur/todos/index"
    }
}