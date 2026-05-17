package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Origin(botUtils: BotUtils, botConfig: BotConfig) :
    MessageHandler(botUtils, botConfig), HasHalp {

    private val tgbot by lazy { getTgBot() }
    private val regex = Regex("^!origin(i)?$", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))

    override fun handle(message: Message) {
        if (regex.matches(message.text)) {
            botUtils.rispondi(ActionResponse.document("", tgbot), message, false)
        }
    }

    override fun halp() = "<b>!origin</b> le origini di Sgrunty, da dove tutto è partito"

    private fun getTgBot(): InputFile {
        val tgbot = ClassPathResource("tgbot.rb")
        return InputFile(tgbot.inputStream, tgbot.filename)
    }

}