package org.ldv.todo0.controller

import org.ldv.todo0.model.dao.CategorieDao
import org.ldv.todo0.model.dao.TodoDao
import org.ldv.todo0.model.entity.Todo
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

/**
 * Contrôleur de gestion des Todos dans l'espace administrateur.
 *
 * Ce contrôleur permet aux administrateurs de :
 * - consulter la liste des Todos
 * - créer un nouveau Todo
 * - modifier un Todo existant
 * - supprimer un Todo
 *
 * Il s'appuie sur Spring MVC, Spring Data JPA et Thymeleaf
 * pour implémenter un CRUD complet.
 *
 * @param todoDao DAO permettant l'accès aux Todos en base de données
 * @param categorieDao DAO permettant l'accès aux catégories (association Todo → Catégorie)
 */
@Controller
class AdminTodoController(
    private val todoDao: TodoDao,
    private val categorieDao: CategorieDao
) {

    /**
     * Affiche la liste de tous les Todos.
     *
     * Les Todos sont récupérés triés par titre (ordre alphabétique).
     *
     * @param model objet permettant de transmettre les Todos à la vue
     * @return le template affichant la liste des Todos
     */
    @GetMapping("/todoapp/admin/todos")
    fun index(model: Model): String {
        val todos = todoDao.findByOrderByTitreAsc()
        model.addAttribute("todos", todos)
        return "pagesAdmin/todo/index"
    }


    /**
     * Affiche le formulaire de création d'un nouveau Todo.
     *
     * Une liste de catégories est fournie afin de permettre
     * l'association du Todo à une catégorie existante.
     *
     * @param model objet permettant de transmettre le Todo et les catégories à la vue
     * @return le template du formulaire de création
     */
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

    /**
     * Traite la soumission du formulaire de création d'un Todo.
     *
     * @param todo Todo reconstruit automatiquement depuis le formulaire
     * @param redirectAttributes permet d'envoyer un message flash
     * @return redirection vers la liste des Todos
     */
    @PostMapping("/todoapp/admin/todos")
    fun store(
        @ModelAttribute todo: Todo,
        redirectAttributes: RedirectAttributes
    ): String {
        todoDao.save(todo)
        redirectAttributes.addFlashAttribute(
            "msg",
            "Le Todo a bien été créé"
        )
        return "redirect:/todoapp/admin/todos"
    }

    /**
     * Affiche le formulaire d'édition d'un Todo existant.
     *
     * @param id identifiant du Todo à modifier
     * @param model objet permettant de transmettre le Todo et les catégories à la vue
     * @return le template du formulaire de modification
     */
    @GetMapping("/todoapp/admin/todos/edit/{id}")
    fun edit(@PathVariable id: Long, model: Model): String {
        val todo = todoDao.findById(id).orElseThrow()
        val categories = categorieDao.findAll()

        model.addAttribute("categories", categories)
        model.addAttribute("todo", todo)

        return "pagesAdmin/todo/edit"
    }

    /**
     * Traite la soumission du formulaire de modification d'un Todo.
     *
     * @param todo Todo modifié
     * @param redirectAttributes permet d'envoyer un message flash
     * @return redirection vers la liste des Todos
     */
    @PostMapping("/todoapp/admin/todos/update")
    fun update(
        @ModelAttribute todo: Todo,
        redirectAttributes: RedirectAttributes
    ): String {
        todoDao.save(todo)
        redirectAttributes.addFlashAttribute(
            "msg",
            "Le Todo '${todo.titre}' a bien été modifié."
        )
        return "redirect:/todoapp/admin/todos"
    }

    /**
     * Supprime un Todo.
     *
     * @param id identifiant du Todo à supprimer
     * @param redirectAttributes permet d'envoyer un message flash
     * @return redirection vers la liste des Todos
     */
    @PostMapping("/todoapp/admin/todos/delete")
    fun delete(
        @RequestParam id: Long,
        redirectAttributes: RedirectAttributes
    ): String {
        todoDao.deleteById(id)
        redirectAttributes.addFlashAttribute(
            "msg",
            "Le todo a bien été supprimé."
        )
        return "redirect:/todoapp/admin/todos"
    }
}
