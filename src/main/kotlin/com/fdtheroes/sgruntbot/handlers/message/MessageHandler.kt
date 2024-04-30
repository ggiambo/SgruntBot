package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.handlers.Handler
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.time.LocalDateTime
import kotlin.random.Random

abstract class MessageHandler(protected val botUtils: BotUtils, protected val botConfig: BotConfig) : Handler {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val lastAuthorRegex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    override suspend fun handle(update: Update) {
        if (botConfig.pausedTime != null) {
            if (LocalDateTime.now() < botConfig.pausedTime) {
                botConfig.pausedTime = null
                log.info("Posso parlare di nuovo!")
            } else {
                return
            }
        }

        val message = update.message
        if (message == null) {
            return
        }

        if (message.text.isNullOrEmpty()) {
            return
        }

        if (!lastAuthorRegex.containsMatchIn(message.text) && botUtils.isMessageInChat(message)) {
            botConfig.lastAuthor = message.from
        }

        botConfig.pignolo = Random.nextInt(100) > 90

        handle(update.message)
    }

    abstract fun handle(message: Message)

}