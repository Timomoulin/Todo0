package org.ldv.todo0.model.dao

import org.ldv.todo0.model.entity.Utilisateur
import org.springframework.data.jpa.repository.JpaRepository

interface UtilisateurDao : JpaRepository<Utilisateur, Long> {


    fun findByEmail(email: String): Utilisateur?

}