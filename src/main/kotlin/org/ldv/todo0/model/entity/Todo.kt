package org.ldv.todo0.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.PreUpdate
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
class Todo (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null,
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