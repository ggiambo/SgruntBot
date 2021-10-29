package com.fdtheroes.sgruntbot.scheduled

import org.telegram.telegrambots.meta.api.methods.send.SendMessage

class RandomFortune(
    sendMessage: (SendMessage) -> Unit,
    private val getFortuneText: () -> String,
) : ScheduledAction(sendMessage) {

    override fun getMessageText() = getFortuneText()

}