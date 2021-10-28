package com.fdtheroes.sgruntbot.scheduled

import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class RandomFortune(
    context: Context,
    sendMessage: (SendMessage) -> Unit,
    private val getFortuneText: () -> String,
) : ScheduledAction(context, sendMessage) {

    override fun getMessageText() = getFortuneText()

}