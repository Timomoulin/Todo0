package org.ldv.todo0.controller

import jakarta.validation.Valid
import org.ldv.todo0.model.dao.CategorieDao
import org.ldv.todo0.model.dao.TodoDao
import org.ldv.todo0.model.entity.Todo
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Controller
class UtilisateurTodoController(
    private val todoDao: TodoDao,
    private val categorieDao: CategorieDao
) {
    private val logger = LoggerFactory.getLogger(UtilisateurTodoController::class.java)
    private val auditLogger = LoggerFactory.getLogger("AUDIT")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @PreAuthorize("hasAnyRole('UTILISATEUR','ADMIN')")
    @GetMapping("/todoapp/utilisateur/todos")
    fun index(model: Model): String {
        val (username, ip) = getCurrentUserInfo()

        logger.info("Utilisateur $username depuis IP $ip accède à la liste des todos")

        val lesTodos = todoDao.findAll()
        model.addAttribute("todos", lesTodos)
        return "pagesUtilisateur/todos/index"
    }


    @GetMapping("/todoapp/utilisateur/todos/create")
    fun create(model: Model): String {
        val (username, ip) = getCurrentUserInfo()
        logger.info("Utilisateur $username depuis IP $ip accède au formulaire de création d'un Todo")

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

        val (username, ip) = getCurrentUserInfo()

        if (bindingResult.hasErrors()) {
            logger.warn("Échec de création du Todo par $username depuis IP $ip : erreurs de validation")
            model.addAttribute("todo", todo)
            model.addAttribute("categories", categorieDao.findAll())
            return "pagesUtilisateur/todos/create"
        }

        val nouveauTodo = todoDao.save(todo)

        // Log audit pour action sensible
        auditLogger.info("[${LocalDateTime.now().format(dateFormatter)}] Utilisateur $username depuis IP $ip a créé le Todo '${nouveauTodo.titre}' (id=${nouveauTodo.id})")

        redirectAttributes.addFlashAttribute(
            "msg",
            "Le Todo ${todo.titre} a bien été créé."
        )
        return "redirect:/todoapp/utilisateur/todos"
    }
    private fun getCurrentUserInfo(): Pair<String, String> {
        val username = org.springframework.security.core.context.SecurityContextHolder
            .getContext().authentication?.name ?: "ANONYME"

        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
        val ip = request?.remoteAddr ?: "IP inconnue"

        return username to ip
    }

}
