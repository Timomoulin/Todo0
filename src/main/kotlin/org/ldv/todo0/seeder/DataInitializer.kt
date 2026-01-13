package org.ldv.todo0.seeder

import org.ldv.todo0.model.dao.CategorieDao
import org.ldv.todo0.model.dao.RoleDao
import org.ldv.todo0.model.dao.TodoDao
import org.ldv.todo0.model.dao.UtilisateurDao
import org.ldv.todo0.model.entity.Categorie
import org.ldv.todo0.model.entity.Role
import org.ldv.todo0.model.entity.Utilisateur
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializer (
    var categorieDao: CategorieDao,
    var todoDao: TodoDao,
    var roleDao: RoleDao,
    var utilisateurDao: UtilisateurDao,
    val  passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (categorieDao.count() == 0L) {
            val catLoisir = Categorie(nom = "Loisir", couleur = "#FF0000")
            val catTravail = Categorie(nom = "Travail", couleur = "#aaaaaa")
            categorieDao.saveAll(mutableListOf(catLoisir, catTravail))
            println("✅ Données initiales insérées : ${categorieDao.count()} catégories.")
        }

        if (roleDao.count() == 0L) {
            val roleAdmin = Role(
                nom = "ADMIN"
            )

            val roleUtilisateur = Role(
                nom = "UTILISATEUR"
            )

            roleDao.saveAll(listOf(roleAdmin, roleUtilisateur))
            println("✅ Données initiales insérées : ${roleDao.count()} roles.")

            if (utilisateurDao.count() == 0L) {
                val admin = Utilisateur(
                    id = null,
                    nom = "Super",
                    prenom = "Admin",
                    email = "admin@admin.com",
                    mdp = passwordEncoder.encode("admin123")!!, // mot de passe hashé
                    role = roleAdmin
                )

                val utilisateur = Utilisateur(
                    id = null,
                    nom = "Jean",
                    prenom = "Client",
                    email = "client@client.com",
                    mdp = passwordEncoder.encode("client123")!!, // mot de passe hashé
                    role = roleUtilisateur
                )
            }
        }
    }

}