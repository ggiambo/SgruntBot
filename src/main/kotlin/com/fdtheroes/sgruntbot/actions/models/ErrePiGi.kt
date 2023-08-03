package com.fdtheroes.sgruntbot.actions.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table("errepigi")
class ErrePiGi(
    @Id
    @Column("user_id")
    var userId: Long,
    @Column("hp") var hp: Int = 10,
    @Column("attaccanti_ids") var attaccantiIds: String = "" // lista di ids, LOL
)