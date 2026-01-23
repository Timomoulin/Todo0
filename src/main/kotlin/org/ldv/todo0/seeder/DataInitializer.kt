package org.ldv.todo0.seeder

import org.ldv.todo0.model.dao.CategorieDao
import org.ldv.todo0.model.dao.RoleDao
import org.ldv.todo0.model.dao.TodoDao
import org.ldv.todo0.model.dao.UtilisateurDao
import org.ldv.todo0.model.entity.Categorie
import org.ldv.todo0.model.entity.Role
import org.ldv.todo0.model.entity.Todo
import org.ldv.todo0.model.entity.Utilisateur
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

/**
 * Composant Spring chargé d'initialiser des données de démonstration
 * au démarrage de l'application.
 *
 * Cette classe est exécutée automatiquement grâce à l'interface
 * [CommandLineRunner].
 *
 * Elle permet de :
 * - créer des catégories
 * - créer des todos
 * - créer des rôles
 * - créer des utilisateurs
 *
 * Les données ne sont insérées que si les tables correspondantes sont vides.
 */
@Component
class DataInitializer(
    /** Accès aux données des catégories */
    var categorieDao: CategorieDao,

    /** Accès aux données des todos */
    var todoDao: TodoDao,

    /** Accès aux données des rôles */
    var roleDao: RoleDao,

    /** Accès aux données des utilisateurs */
    var utilisateurDao: UtilisateurDao,

    /** Encodeur de mot de passe (BCrypt, Argon2, etc.) */
    val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    /**
     * Méthode exécutée automatiquement au démarrage de l'application.
     *
     * @param args arguments passés en ligne de commande (non utilisés ici)
     */
    override fun run(vararg args: String) {

        /**
         * Initialisation des catégories et des todos
         * uniquement si la table catégorie est vide.
         */
        if (categorieDao.count() == 0L) {

            val catLoisir = Categorie(
                nom = "Loisir",
                couleur = "#FF0000"
            )

            val catTravail = Categorie(
                nom = "Travail",
                couleur = "#aaaaaa"
            )

            categorieDao.saveAll(mutableListOf(catLoisir, catTravail))
            println("✅ Données initiales insérées : ${categorieDao.count()} catégories.")

            /**
             * Création de todos uniquement si la table todo est vide.
             */
            if (todoDao.count() == 0L) {

                val todoVideo = Todo(
                    titre = "Faire une vidéo de démonstration",
                    description = "Vidéo de démonstration de l'application Todo0",
                    categorie = catTravail
                )

                val todoSport = Todo(
                    titre = "Faire du sport",
                    description = "Faire du sport",
                    categorie = catLoisir
                )

                todoDao.saveAll(listOf(todoVideo, todoSport))
                println("✅ Données initiales insérées : ${todoDao.count()} todos.")
            }
        }

        /**
         * Initialisation des rôles de sécurité
         * uniquement si la table rôle est vide.
         */
        if (roleDao.count() == 0L) {

            val roleAdmin = Role(nom = "ADMIN")
            val roleUtilisateur = Role(nom = "UTILISATEUR")

            roleDao.saveAll(listOf(roleAdmin, roleUtilisateur))
            println("✅ Données initiales insérées : ${roleDao.count()} rôles.")

            /**
             * Initialisation des utilisateurs uniquement si la table utilisateur est vide.
             */
            if (utilisateurDao.count() == 0L) {

                val admin = Utilisateur(
                    id = null,
                    nom = "Super",
                    prenom = "Admin",
                    email = "admin@admin.com",
                    mdp = passwordEncoder.encode("Tod0@dmin123")!!,
                    role = roleAdmin
                )

                val utilisateur = Utilisateur(
                    id = null,
                    nom = "Jean",
                    prenom = "Client",
                    email = "client@client.com",
                    mdp = passwordEncoder.encode("Tod0€lient123")!!,
                    role = roleUtilisateur
                )

                utilisateurDao.saveAll(listOf(admin, utilisateur))
            }
        }
    }
}
