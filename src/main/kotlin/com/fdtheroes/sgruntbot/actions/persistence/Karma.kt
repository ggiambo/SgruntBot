package com.fdtheroes.sgruntbot.actions.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("karma")
data class Karma(
    @Id
    @Column("user_id")
    var userId: Long,
    var karma: Int,
    @Column("karma_credit") var karmaCredit: Int,
    @Column("credit_updated") var creditUpdated: LocalDate
)
