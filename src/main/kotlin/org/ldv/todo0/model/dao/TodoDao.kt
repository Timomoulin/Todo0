package org.ldv.todo0.model.dao

import org.ldv.todo0.model.entity.Todo
import org.springframework.data.jpa.repository.JpaRepository

interface TodoDao : JpaRepository<Todo, Long> {
}