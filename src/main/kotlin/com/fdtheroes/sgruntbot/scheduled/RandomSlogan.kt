package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class RandomSlogan(
    context: Context,
    sendMessage: (SendMessage) -> Unit,
    private val getSloganText: (String) -> String,
) : ScheduledAction(context, sendMessage) {

    override fun getMessageText(): String {
        val lastAuthor = context.lastAuthor
        if (lastAuthor.isNullOrEmpty()) {
            return getSloganText("")
        }

        return getSloganText(lastAuthor)
    }

}