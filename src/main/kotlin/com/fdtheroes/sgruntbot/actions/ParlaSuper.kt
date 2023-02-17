package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.Users
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class ParlaSuper(
    sgruntBot: SgruntBot,
    private val botConfig: BotConfig
) : Action(sgruntBot), HasHalp {

    private val regex = Regex(
        "^!parlasuper (.*)$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun doAction(message: Message) {
        val testo = regex.find(message.text)?.groupValues?.get(1)
        if (testo != null && Users.byId(message.from.id) != null) {
            sgruntBot.rispondi(SendMessage(botConfig.chatId.toString(), testo))
            botConfig.lastSuper = message.from
        }
    }

    override fun halp() =
        "<b>!parlasuper</b> <i>testo</i> fai dire qualcosa a Sgrunty. Da usare in una chat privata con lui."
}
