package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Youtube(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val regex = Regex("^!yt (.*)$", RegexOption.IGNORE_CASE)
    private val suoraProxy = "http://198.98.49.55:8118"

    override fun handle(message: Message) {
        val canzone = regex.find(message.text)?.groupValues?.get(1)
        if (canzone != null) {
            val id = fetch(canzone)
            if (id == null) {
                botUtils.rispondi(ActionResponse.message("Non ci riesco."), message)
                return
            }
            val youtubeLink = "https://www.youtube.com/watch?v=$id"
            botUtils.rispondi(ActionResponse.message(youtubeLink), message)
        }
    }

    override fun halp() = "<b>!canzone</b> <i>la tua canzone</i> cerca e scarica la tua canzone"

    private fun fetch(query: String): String? {
        val command =
            """yt-dlp --get-id --proxy $suoraProxy "ytsearch1:$query" --geo-bypass-country IT 2>&1"""
        log.info(command)
        return ProcessBuilder()
            .command("sh", "-c", command)
            .start()
            .inputStream
            .bufferedReader()
            .readLines()
            .last()

    }

}
