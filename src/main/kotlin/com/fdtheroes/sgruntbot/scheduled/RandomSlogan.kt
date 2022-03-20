package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.User

class RandomSlogan(
    sendMessage: (SendMessage) -> Unit,
    private val getSloganText: (User) -> String,
) : RandomScheduledAction(sendMessage) {

    override fun getMessageText(): String {
        val lastAuthor = Context.lastAuthor
        if (lastAuthor == null) {
            return ""
        }

        return getSloganText(lastAuthor)
    }

}