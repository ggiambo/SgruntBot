package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.handlers.Handler
import com.fdtheroes.sgruntbot.utils.BotUtils
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import org.telegram.telegrambots.util.WebhookUtils

@Service
class SgruntBot(
    private val botConfig: BotConfig,
    private val botUtils: BotUtils,
    private val handlers: List<Handler>,
) : LongPollingBot {

    private val log = LoggerFactory.getLogger(this.javaClass)
    val coroutineScope = CoroutineScope(SupervisorJob())

    @PostConstruct
    fun postConstruct() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
        log.info("Sono partito!")
    }

    override fun getBotUsername() = botConfig.botName

    override fun getBotToken() = botConfig.token

    override fun getOptions() = botConfig.defaultBotOptions

    override fun clearWebhook() = WebhookUtils.clearWebhook(botUtils)

    override fun onClosing() = botUtils.shutdown()

    override fun onUpdateReceived(update: Update) {
        coroutineScope.launch {
            handlers.forEach {
                it.handle(update)
            }
        }
    }

}
