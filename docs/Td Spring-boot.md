# TP – Application Todo avec Spring Boot (Kotlin)

**Formation / TP – Spring Boot**  
**Public :** BTS SIO  
**Objectif :** Mettre en œuvre une application Web Spring Boot complète autour de la gestion de Todos, en utilisant Kotlin, Thymeleaf et Spring Security.

---

## 1. Création du contrôleur `UtilisateurTodoController`

### Création de la classe

- Faire un clic droit sur le package `controller`
- Sélectionner **New → Kotlin Class/File**


!["Nouveau projet"](img/1_new_projet.png)

### Annotation du contrôleur

Annoter la classe afin qu’elle soit reconnue par Spring comme un contrôleur MVC.

```kotlin
@Controller
class UtilisateurTodoController {
}
```

> 💡 Il est recommandé d’utiliser l’auto‑complétion d’IntelliJ IDEA afin d’importer correctement l’annotation `@Controller`.

---

## 2. Injection des DAO dans le contrôleur

Nous souhaitons injecter le `TodoDao` dans notre contrôleur afin d’accéder aux données.

L’injection se fait via le constructeur principal de la classe :

```kotlin
@Controller
class UtilisateurTodoController(
    private val todoDao: TodoDao
) {
}
```

> ℹ️ Le même principe s’applique pour l’injection dans une classe `@Service` ou `@Component`.

---

## 3. Création d’un mapping pour afficher les Todos

Nous allons créer une méthode dans le contrôleur. Le nom de la méthode n’a pas d’importance ; ici nous utilisons `index`.

Cette méthode doit répondre à une requête HTTP **GET**, nous utilisons donc l’annotation `@GetMapping`.

```kotlin
@GetMapping("/todoapp/utilisateur/todos")
fun index(): String {
    return "pagesUtilisateur/todos/index"
}
```

- `@GetMapping` précise la méthode HTTP utilisée
- L’argument correspond à l’endpoint exposé par l’application

---

## 4. Affichage d’une vue Thymeleaf

### Création du template

Créer le fichier suivant :

```
src/main/resources/templates/pagesUtilisateur/todos/index.html
```
!["Création index.html"](img/5_vue_index.png)

Faire un copier/coller de template.html  et ajouter un h1 pour obtenir :

!["Code index.html"](img/6_code_index.png)

> ⚠️ Si la chaîne retournée par la méthode index() du controlleur n’est pas soulignée dans IntelliJ, cela indique que le chemin vers le template est incorrect.

---

## 5. Test fonctionnel

Démarrer l’application puis accéder à l’URL suivante :

```
http://localhost:8080/todoapp/utilisateur/todos
```

Vous devez voir s’afficher le titre **Ma liste de todos**.

---

## 6. Récupération des Todos depuis la base de données

Notre objectif est maintenant de récupérer les Todos enregistrés en base de données.

Nous allons pour cela utiliser le `TodoDao` et sa méthode `findAll()`.

```kotlin
@GetMapping("/todoapp/utilisateur/todos")
fun index(model: Model): String {
    val todos = todoDao.findAll()
    model.addAttribute("todos", todos)
    return "pagesUtilisateur/todos/index"
}
```

- Le paramètre `Model` permet de transmettre des données du contrôleur vers la vue
- La clé `todos` sera utilisée dans le template

---

## 7. Mise à jour du template d’affichage

Nous utilisons Thymeleaf pour parcourir la liste des Todos et les afficher dynamiquement.

```html
<div class="container">
    <h1>Ma liste de todos</h1>
    <ul>
        <li th:each="unTodo : ${todos}"
            th:style="${'color :' + unTodo.categorie.couleur + ';'}">
            <h3 th:text="${unTodo.titre}"></h3>
            <p th:text="${'Description : ' + unTodo.description}"></p>
        </li>
    </ul>
</div>
```

- `th:each` permet de parcourir la collection
- Le style est déterminé par la couleur de la catégorie

---

## 8. Affichage du formulaire de création d’un Todo

### Injection de `CategorieDao`

Afin de récupérer les catégories depuis la base de données, nous injectons également le `CategorieDao`.

```kotlin
class UtilisateurTodoController(
    private val todoDao: TodoDao,
    private val categorieDao: CategorieDao
)
```

---

### Création de la méthode `create`

```kotlin
@GetMapping("/todoapp/utilisateur/todos/create")
fun create(model: Model): String {
    val lesCategories = categorieDao.findAll()
    val nouveauTodo = Todo(titre = "", description = "")

    model.addAttribute("categories", lesCategories)
    model.addAttribute("todo", nouveauTodo)

    return "pagesUtilisateur/todos/create"
}
```

> 💡 L’envoi d’un objet `Todo` vide permet de lier directement les champs du formulaire aux propriétés de l’objet.

---

## 9. Création du formulaire de création

Créer le template suivant :

```
templates/pagesUtilisateur/todos/create.html
```

(Le code HTML / Thymeleaf du formulaire est identique à celui fourni dans le document initial.)

---

## 10. Traitement du formulaire de création

Nous créons maintenant une méthode `store` afin de traiter l’envoi du formulaire.

```kotlin
@PostMapping("/todoapp/utilisateur/todos")
fun store(todo: Todo): String {
    todoDao.save(todo)
    return "redirect:/todoapp/utilisateur/todos"
}
```

> ⚠️ Il est important d’appliquer le principe **Post / Redirect / Get** afin d’éviter la soumission multiple du formulaire.

---

## 11. Validation back‑end du formulaire

### Ajout de la dépendance de validation

Dans le fichier `build.gradle.kts` :

```kotlin
implementation("org.springframework.boot:spring-boot-starter-validation")
```

---

### Contraintes de validation dans l’entité `Todo`

```kotlin
@field:NotBlank(message = "Le titre est obligatoire")
@field:Size(min = 3, max = 255, message = "Le titre doit contenir entre 3 et 255 caractères")
var titre: String
```

---

### Mise à jour de la méthode `store`

```kotlin
@PostMapping("/todoapp/utilisateur/todos")
fun store(
    @ModelAttribute @Valid todo: Todo,
    bindingResult: BindingResult,
    model: Model,
    redirectAttributes: RedirectAttributes
): String {

    if (bindingResult.hasErrors()) {
        model.addAttribute("todo", todo)
        model.addAttribute("categories", categorieDao.findAll())
        return "pagesUtilisateur/todos/create"
    }

    todoDao.save(todo)

    redirectAttributes.addFlashAttribute(
        "msg",
        "Le Todo ${todo.titre} a bien été créé."
    )

    return "redirect:/todoapp/utilisateur/todos"
}
```

---

## 12. Affichage des erreurs de validation

Sous le champ du titre, ajouter :

```html
<div class="text-danger"
     th:if="${#fields.hasErrors('titre')}"
     th:errors="*{titre}">
</div>
```

---

## 13. Sécurisation de l’application

### Sécurisation des routes

Deux approches sont possibles :

- **Approche globale** via le `SecurityConfig`
- **Approche fine** via des annotations `@PreAuthorize`

Exemple :

```kotlin
@PreAuthorize("hasAnyRole('UTILISATEUR','ADMIN')")
```

> ℹ️ Cette approche nécessite l’activation de la sécurité par méthode avec `@EnableMethodSecurity`.

---

## 14. Protection CSRF

Spring Security active par défaut la protection contre les attaques CSRF.

Pour les requêtes POST, PUT, PATCH ou DELETE, un token doit être transmis dans les formulaires :

```html
<input type="hidden" name="${_csrf.parameterName}" th:value="${_csrf.token}">
```

Ce champ doit être ajouté à l’ensemble des formulaires.

---

## 15. Conclusion

Ce TP permet de mettre en pratique :
- L’architecture MVC avec Spring Boot
- L’utilisation de Kotlin dans un contexte professionnel
- La gestion de formulaires et de la validation back‑end
- Les bases de la sécurisation d’une application Web

---

**Fin du TP**

