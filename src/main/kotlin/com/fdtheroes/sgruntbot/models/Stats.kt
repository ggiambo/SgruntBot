package com.fdtheroes.sgruntbot.models

import jakarta.persistence.*
import java.time.LocalDate

@Entity(name = "stats")
data class Stats(
    @Column(name = "user_id") var userId: Long,
    @Column(name = "stat_day") var statDay: LocalDate = LocalDate.now(),
    var messages: Int,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
)