package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.net.URLDecoder

@Service
class Slogan(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!slogan (.*)\$", RegexOption.IGNORE_CASE)
    private val sloganPlaceholder = "XXX-XXX-XXX"
    private val urlSlogan = "https://getsocio.com/tools/slogan-generator/generate"

    override fun handle(message: Message) {
        val testo = regex.find(message.text)?.groupValues?.get(1)
        if (testo != null) {
            val slogan = URLDecoder.decode(fetchSlogan(testo), Charsets.UTF_8)
            botUtils.rispondi(ActionResponse.message(slogan), message)
        }
    }

    override fun halp() = "<b>!slogan</b> <i>testo</i> uno slogan per il testo!"

    fun fetchSlogan(testo: String): String {
        val res = botUtils.textFromURL(urlSlogan, listOf("keyword" to testo))
        return Regex("<a.*?>(.*)</a>")
            .findAll(res)
            .take(15) // I primi 15 sono slogan
            .map { it.groupValues[1] }
            .toList()
            .random()
    }

    fun fetchSlogan(utente: User): String {
        val res = fetchSlogan(sloganPlaceholder)
        return res.replace(sloganPlaceholder, botUtils.getUserLink(utente))
    }

}
