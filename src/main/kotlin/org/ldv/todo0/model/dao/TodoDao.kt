package org.ldv.todo0.model.dao

import org.ldv.todo0.model.entity.Todo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TodoDao : JpaRepository<Todo, Long> {


    @Query("select t from Todo t order by t.titre")
    fun findByOrderByTitreAsc(): List<Todo>

}