package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.handlers.Handler
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.objects.Update


@Service
class SgruntBot(
    private val botConfig: BotConfig,
    private val handlers: List<Handler>,
) : LongPollingSingleThreadUpdateConsumer {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @PostConstruct
    fun postConstruct() {
        TelegramBotsLongPollingApplication().registerBot(
            botConfig.telegramToken,
            botConfig.defaultUrl,
            botConfig.allowedUpdates,
            this
        )
        log.info("Sono partito!")
    }

    override fun consume(update: Update) {
        handlers.forEach {
            CoroutineScope(Dispatchers.Default).launch { it.handle(update) }
        }
    }

}
