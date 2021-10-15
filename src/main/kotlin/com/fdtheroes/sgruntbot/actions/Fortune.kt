package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.telegram.telegrambots.meta.api.objects.Message

class Fortune : Action {

    private val regex = Regex("^!(fortune|quote)", RegexOption.IGNORE_CASE)

    override fun doAction(message: Message, context: Context) {
        if (regex.containsMatchIn(message.text)) {
            val fortune = Runtime.getRuntime().exec("fortune -sa it")
                .inputStream
                .reader()
                .readText()
            BotUtils.instance.rispondi(message, fortune)
        }
    }

}