package com.fdtheroes.sgruntbot.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDate

@Entity(name = "scommessina")
data class Scommessina(
    @Column(name = "user_id") var userId: Long,
    var content: String,
    var created: LocalDate = LocalDate.now(),
    @Column(name = "message_id") var messageId: Int,
    @Column(name = "participants_user_id") var participantsUserId : List<Long> = emptyList(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
)