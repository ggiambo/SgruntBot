package com.fdtheroes.sgruntbot.actions.models

import jakarta.persistence.*
import java.time.LocalDate

@Entity(name = "todos")
data class Todos(
    @Column(name = "user_id") var userId: Long,
    @Column(name = "updated") var updated: LocalDate = LocalDate.now(),
    @Column(name = "todo") var todo: String,
    @Column(name = "open") var open: Boolean,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
)
