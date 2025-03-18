package com.fdtheroes.sgruntbot.handlers.message.oroscopo

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.handlers.message.HasHalp
import com.fdtheroes.sgruntbot.handlers.message.MessageHandler
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Oroscopo(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!oroscopo(.*)\$", RegexOption.IGNORE_CASE)
    private val llm = LLM()

    override fun handle(message: Message) {
        if (regex.matches(message.text)) {
            val segno = regex.find(message.text)?.groupValues?.get(1)?.trim()?.lowercase()
            
            val testo = llm.getHoroscope(segno!!)

            botUtils.rispondi(ActionResponse.message(testo), message)
        }
    }


    override fun halp() = "<b>!oroscopo segno</b> per sapere quale sfiga si abbatterà su di te"
}