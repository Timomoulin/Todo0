package org.ldv.todo0.model.dao

import org.ldv.todo0.model.entity.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface RoleDao : JpaRepository<Role, Long> {


    @Query("select r from Role r where upper(r.nom) = upper(?1)")
    fun findByNomIgnoreCase(nom: String): Optional<Role>

}