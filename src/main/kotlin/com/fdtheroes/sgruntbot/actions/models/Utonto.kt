package com.fdtheroes.sgruntbot.actions.models

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table("utonti")
data class Utonto(
    @Column("user_id")
    var userId: Long,
    @Column("first_name")
    var firstName: String,
    @Column("last_name")
    var lastName: String?,
    @Column("user_name")
    var userName: String?,
    @Column("is_bot")
    var isBot: Boolean,
    @Column("updated")
    var updated: LocalDate = LocalDate.now(),
)
