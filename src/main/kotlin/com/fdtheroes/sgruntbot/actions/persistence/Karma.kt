package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.actions.persistence.KarmaRepository.Companion.dailyKarmaCredit
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("karma")
data class Karma(
    @Id
    @Column("user_id")
    var userId: Long,
    var karma: Int = 0,
    @Column("karma_credit") var karmaCredit: Int = dailyKarmaCredit,
    @Column("credit_updated") var creditUpdated : LocalDate = LocalDate.now()
)
