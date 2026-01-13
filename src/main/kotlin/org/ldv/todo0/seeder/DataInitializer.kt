package org.ldv.todo0.seeder

import org.ldv.todo0.model.dao.CategorieDao
import org.ldv.todo0.model.dao.TodoDao
import org.ldv.todo0.model.entity.Categorie
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer (
    var categorieDao: CategorieDao,
    var todoDao: TodoDao
) : CommandLineRunner {

    override fun run(vararg args: String) {
    if (categorieDao.count() == 0L){
       val catLoisir = Categorie(nom = "Loisir", couleur = "#FF0000")
        val catTravail = Categorie(nom = "Travail", couleur = "#aaaaaa")
       categorieDao.saveAll( mutableListOf(catLoisir, catTravail))
        println("✅ Données initiales insérées : ${categorieDao.count()} catégories.")
    }

    }
}