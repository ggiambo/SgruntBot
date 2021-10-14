package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.objects.Message
import java.net.URL

class Slogan : Action {

    private val regex = Regex("^!slogan (.*)\$", setOf(RegexOption.IGNORE_CASE))

    override fun doAction(message: Message, context: Context) {
        val testo = regex.findAll(message.text)
            .map { it.groupValues[1] }
            .firstOrNull()
        if (testo != null) {
            val slogan = fetchSlogan(testo)
            BotUtils.instance.rispondiAsText(message, slogan)
        }
    }

    private fun fetchSlogan(testo: String): String {
        val res = URL("http://www.sloganizer.net/en/outbound.php?slogan=${testo}")
            .openConnection()
            .getInputStream()
            .readAllBytes()
            .decodeToString()
        return Regex("<a.*?>(.*)</a>")
            .findAll(res)
            .map { it.groupValues[1] }
            .firstOrNull()
            .orEmpty()
    }

}