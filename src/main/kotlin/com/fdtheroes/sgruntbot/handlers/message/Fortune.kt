package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Fortune(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!(fortune|quote)", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            botUtils.rispondi(ActionResponse.message(getFortune()), message)
        }
    }

    override fun halp() = "<b>!fortune</b> oppure <b>!quote</b> per una perla di saggezza"

    fun getFortune(): String {
        return Runtime.getRuntime().exec(arrayOf("fortune", "-sa it"))
            .inputStream
            .reader()
            .readText()
    }

}
