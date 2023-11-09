package com.fdtheroes.sgruntbot.actions.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDate

@Entity(name = "todos")
data class Todos(
    @Column(name = "user_id") var userId: Long,
    @Column(name = "updated") var updated: LocalDate = LocalDate.now(),
    @Column(name = "todo") var todo: String,
    @Column(name = "open") var open: Boolean,
    @Id @Column(name = "id") var id: Long? = null,
)
