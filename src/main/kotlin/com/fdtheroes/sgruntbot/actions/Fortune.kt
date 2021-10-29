package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import org.telegram.telegrambots.meta.api.objects.Message

class Fortune : Action {

    private val regex = Regex("^!(fortune|quote)", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            BotUtils.rispondi(message, getFortune())
        }
    }

    companion object {
        fun getFortune(): String {
            return Runtime.getRuntime().exec("fortune -sa it")
                .inputStream
                .reader()
                .readText()
        }
    }

}