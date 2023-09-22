package com.fdtheroes.sgruntbot.actions.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDate

@Entity(name = "utonti")
data class Utonto(
    @Column(name = "first_name", nullable = false)
    var firstName: String,
    @Column(name = "last_name")
    var lastName: String?,
    @Column(name = "user_name")
    var userName: String?,
    @Column(name = "is_bot", nullable = false)
    var isBot: Boolean,
    @Column(name = "updated", nullable = false)
    var updated: LocalDate = LocalDate.now(),
    @Id @Column(name = "user_id")
    var userId: Long? = null,
)
