package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Last(private val slogan: Slogan, private val botConfig: BotConfig) : Action, HasHalp {

    private val regex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (regex.matches(message.text) && botConfig.lastAuthor != null) {
            val sendMessage = SendMessage()
            sendMessage.chatId = message.chatId.toString()
            sendMessage.parseMode = ParseMode.HTML
            sendMessage.text = slogan.fetchSlogan(botConfig.lastAuthor!!)
            sgruntBot.rispondi(sendMessage)
        }
    }

    override fun halp() = "<b>!last</b> uno slogan per l'ultimo autore"

}
