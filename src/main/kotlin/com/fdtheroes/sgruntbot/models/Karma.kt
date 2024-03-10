package com.fdtheroes.sgruntbot.models

import com.fdtheroes.sgruntbot.persistence.KarmaRepository.Companion.dailyKarmaCredit
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDate

@Entity(name = "karma")
data class Karma(
    var karma: Int = 0,
    @Column(name = "karma_credit") var karmaCredit: Int = dailyKarmaCredit,
    @Column(name = "credit_updated") var creditUpdated: LocalDate = LocalDate.now(),
    @Id @Column(name = "user_id") var userId: Long? = null,
)
