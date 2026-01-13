package org.ldv.todo0.model.dao

import org.ldv.todo0.model.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleDao : JpaRepository<Role, Long> {
}