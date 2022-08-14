package com.fdtheroes.sgruntbot.actions.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("stats")
data class Stats(
    @Id
    var id: Long? = null,
    @Column("user_id")
    var userId: Long,
    @Column("stat_day")
    var statDay: LocalDate = LocalDate.now(),
    var messages: Int,
)