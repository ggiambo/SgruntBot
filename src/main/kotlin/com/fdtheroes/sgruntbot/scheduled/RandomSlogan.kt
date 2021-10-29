package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class RandomSlogan(
    sendMessage: (SendMessage) -> Unit,
    private val getSloganText: (String) -> String,
) : ScheduledAction(sendMessage) {

    override fun getMessageText(): String {
        val lastAuthor = Context.lastAuthor
        if (lastAuthor.isNullOrEmpty()) {
            return getSloganText("")
        }

        return getSloganText(lastAuthor)
    }

}