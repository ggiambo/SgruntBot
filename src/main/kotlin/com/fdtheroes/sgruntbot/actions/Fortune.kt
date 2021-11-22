package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

class Fortune : Action, HasHalp {

    private val regex = Regex("^!(fortune|quote)", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            BotUtils.rispondi(message, getFortune())
        }
    }

    override fun halp() = "<b>!fortune</b> oppure <b>!quote</b> per una perla di saggezza"

    companion object {
        fun getFortune(): String {
            return Runtime.getRuntime().exec("fortune -sa it")
                .inputStream
                .reader()
                .readText()
        }
    }

}