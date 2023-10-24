package com.fdtheroes.sgruntbot.actions.models

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "errepigi")
class ErrePiGi(
    @Column(name = "hp") var hp: Int = 10,
    @Column(name = "attaccanti_ids") var attaccantiIds: String = "", // lista di ids, LOL
    @Id @Column(name = "user_id") var userId: Long? = null,
)
