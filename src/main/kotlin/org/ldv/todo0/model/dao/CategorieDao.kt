package org.ldv.todo0.model.dao

import org.ldv.todo0.model.entity.Categorie
import org.springframework.data.jpa.repository.JpaRepository

interface CategorieDao : JpaRepository<Categorie, Long> {
}