package org.ldv.todo0.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime

@Entity
class Categorie (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,
    var nom:String,
    var couleur:String,
    @OneToMany(
        mappedBy = "categorie",
        cascade = [ CascadeType.REMOVE],
        orphanRemoval = true
    )
    var todos: MutableList<Todo> = mutableListOf(),
    var dateCreation: LocalDateTime = LocalDateTime.now()
){
    var dateModification: LocalDateTime = LocalDateTime.now()



    @PreUpdate
    fun majDateModification() {
        dateModification = LocalDateTime.now()
    }
}