package com.fdtheroes.sgruntbot.actions.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("complimenti")
data class Complimento(
    @Id
    @Column("user_id")
    var userId: Long,
    var complimento: String = "",
)
