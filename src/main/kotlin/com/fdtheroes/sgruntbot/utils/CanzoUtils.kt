package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.Bot
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.objects.Message

class CanzoUtils {

    fun canzo(bot: Bot, message: Message, query: String?) {
        bot.executeAsync(SendChatAction(message.chat.id.toString(), ActionType.UPLOADDOCUMENT.toString()))
        // la suoraccia maledetta si basa su script esterni sconosciuti
        RispondiUtils().rispondi(bot, message, "Non ci riesco.")
    }

}