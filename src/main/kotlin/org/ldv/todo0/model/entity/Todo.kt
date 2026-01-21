package org.ldv.todo0.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PreUpdate
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

/**
 * Représente une tâche à réaliser.
 * Mappée sur une table SQL à l'aide de l'annotation @Entity.
 *
 * @property id Identifiant unique de la tâche, généré automatiquement par la base de données (auto-increment).
 * @property titre Titre de la tâche.
 * @property description Description détaillée de la tâche.
 * @property etreFait Indicateur précisant si la tâche est marquée comme faite ou non.
 * @property dateAFaire Date et heure prévues pour la réalisation de la tâche.
 * @property categorie Catégorie à laquelle la tâche est associée.
 * @property dateCreation Date de création de la tâche, définie automatiquement à l'insertion et non modifiable.
 * @property dateModification Date de dernière modification de la tâche, mise à jour automatiquement à chaque modification.
 */
@Entity
class Todo (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,

    @field:NotBlank(message = "Le titre est obligatoire")
    @field:Size(min = 3, max = 255, message = "Le titre doit contenir entre 3 et 255 caractères")
    val titre:String,

    val description:String,
    val etreFait:Boolean = false,
    val dateAFaire:LocalDateTime? = null,
    @ManyToOne
    @JoinColumn(name = "categorie_id")
    val categorie: Categorie? = null,
    @CreationTimestamp
    @Column(updatable = false)
    val dateCreation: LocalDateTime = LocalDateTime.now(),
    @UpdateTimestamp
    var dateModification: LocalDateTime = LocalDateTime.now()
) {




}