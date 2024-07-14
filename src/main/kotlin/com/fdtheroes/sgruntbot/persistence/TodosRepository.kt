package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.models.Todos
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
interface TodosRepository : JpaRepository<Todos, Long> {

    fun findByIdAndUserIdAndOpen(id: Long, userId: Long, open: Boolean): Todos?

    @Transactional
    fun deleteAllByUserIdNotIn(userIds: List<Long>): List<Todos>

}
