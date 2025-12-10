package org.ldv.todo0.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime

@Entity
class Todo (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    var id: Long? = null,
    var titre:String,
    var description:String,
    var dateCréation: LocalDateTime = LocalDateTime.now()
) {
    var dateModification: LocalDateTime = LocalDateTime.now()
    @PreUpdate
    fun majDateModification() {
        dateModification = LocalDateTime.now()
    }

}