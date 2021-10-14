package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.objects.Message

class Fortune : Action {

    private val regex = Regex("^!(fortune|quote)", setOf(RegexOption.IGNORE_CASE))

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