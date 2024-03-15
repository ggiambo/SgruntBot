package com.fdtheroes.sgruntbot.handlers

import org.telegram.telegrambots.meta.api.objects.Update

fun interface Handler {
    suspend fun handle(update: Update)
}