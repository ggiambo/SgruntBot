package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Parla(
    private val sgruntBot: SgruntBot,
    private val botUtils: BotUtils,
) : Action, HasHalp {

    private val regex = Regex(
        "^!parla (.*)$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun doAction(message: Message) {
        val msg = regex.find(message.text)?.groupValues?.get(1)
        if (msg != null) {
            val sendMessage = SendMessage(botUtils.chatId, "Mi dicono di dire: $msg")
            sgruntBot.rispondi(sendMessage)
        }
    }

    override fun halp() = "<b>!parla</b> <i>testo</i> fai dire qualcosa a Sgrunty"
}
