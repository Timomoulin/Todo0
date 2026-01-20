package org.ldv.todo0.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.PreUpdate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

/**
 * Représente une catégorie utilisée pour organiser les tâches.
 * Mappée sur une table SQL à l'aide de l'annotation @Entity.
 *
 * @property id Identifiant unique de la catégorie, généré automatiquement par la base de données.
 * @property nom Nom ou libellé de la catégorie.
 * @property couleur Couleur associée à la catégorie, utilisée pour l'affichage ou la distinction visuelle.
 * @property todos Liste des tâches associées à cette catégorie.
 * La relation est de type One-to-Many avec cascade sur la suppression et suppression orpheline.
 * @property dateCreation Date de création de la catégorie, définie automatiquement à l'insertion et non modifiable.
 * @property dateModification Date de dernière modification de la catégorie, mise à jour automatiquement à chaque modification.
 */
@Entity
class Categorie (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,
    var nom:String,
    var couleur:String,
    @OneToMany(
        mappedBy = "categorie",
        cascade = [ CascadeType.REMOVE],
        orphanRemoval = true
    )
    val todos: MutableList<Todo> = mutableListOf(),
    @CreationTimestamp
    @Column(updatable = false)
    val dateCreation: LocalDateTime = LocalDateTime.now(),
    @UpdateTimestamp
    var dateModification: LocalDateTime = LocalDateTime.now()
){

}