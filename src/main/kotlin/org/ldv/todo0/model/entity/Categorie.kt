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