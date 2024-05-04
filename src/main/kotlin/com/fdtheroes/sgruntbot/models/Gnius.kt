package com.fdtheroes.sgruntbot.models

import java.time.LocalDateTime

data class Gnius(
    val published: LocalDateTime,
    val title: String,
    val link: String,
)