package com.fdtheroes.sgruntbot.models

import java.time.LocalDateTime

data class Gnius(
    val updated: LocalDateTime,
    val published: LocalDateTime,
    val title: String,
    val link: String,
)