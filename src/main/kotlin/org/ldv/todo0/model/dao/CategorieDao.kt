package org.ldv.todo0.model.dao

import org.ldv.todo0.model.entity.Categorie
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CategorieDao : JpaRepository<Categorie, Long> {
//Utilise le DSL
    fun findByNom(nom: String): Categorie?

//Utilise la syntaxe JPQL
    @Query("select c from Categorie c where upper(c.couleur) = upper(?1)")
    fun filtreParCouleur(couleur: String): List<Categorie>

}