package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.models.Todos
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface TodosRepository : JpaRepository<Todos, Long> {

    fun findByIdAndUserIdAndOpen(id: Long, userId: Long, open: Boolean) : Todos?

}
