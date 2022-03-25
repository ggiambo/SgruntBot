package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import com.fdtheroes.sgruntbot.Users
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class ParlaSuper : Action, HasHalp {

    private val regex = Regex(
        "^!parlasuper (.*)$",
        setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun doAction(message: Message) {
        val testo = regex.find(message.text)?.groupValues?.get(1)
        if (testo != null && Users.byId(message.from.id) != null) {
            BotUtils.rispondi(SendMessage(BotUtils.chatId, testo))
            Context.lastSuper = message.from
        }
    }

    override fun halp() = "<b>!parlasuper</b> <i>testo</i> fai dire qualcosa a Sgrunty. Da usare in una chat privata con lui."
}
