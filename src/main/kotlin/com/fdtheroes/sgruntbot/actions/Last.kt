package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Last(
    private val sgruntBot: SgruntBot,
    private val botUtils: BotUtils,
    private val slogan: Slogan,
) : Action, HasHalp {

    private val regex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.matches(message.text) && Context.lastAuthor != null) {
            val sendMessage = SendMessage()
            sendMessage.chatId = botUtils.chatId
            sendMessage.parseMode = ParseMode.HTML
            sendMessage.text = slogan.fetchSlogan(Context.lastAuthor!!)
            sgruntBot.rispondi(sendMessage)
        }
    }

    override fun halp() = "<b>!last</b> uno slogan per l'ultimo autore"

}
