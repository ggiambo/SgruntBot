package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.actions.Action
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.LongPollingBot
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import org.telegram.telegrambots.util.WebhookUtils
import java.time.LocalDateTime
import kotlin.concurrent.thread
import kotlin.random.Random.Default.nextInt

@Service
class Bot(
    private val botConfig: BotConfig,
    private val botUtils: BotUtils,
    private val actions: List<Action>,
    private val sgruntBot: SgruntBot,
) : LongPollingBot {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val lastAuthorRegex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    @PostConstruct
    fun postConstruct() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
        log.info("Sono partito!")
    }

    override fun getBotUsername() = botConfig.botName

    override fun getBotToken() = botConfig.token

    override fun getOptions() = botConfig.defaultBotOptions

    override fun clearWebhook() = WebhookUtils.clearWebhook(sgruntBot)

    override fun onUpdateReceived(update: Update?) {
        if (botConfig.pausedTime != null) {
            if (LocalDateTime.now() < botConfig.pausedTime) {
                botConfig.pausedTime = null
                log.info("Posso parlare di nuovo!")
            } else {
                return
            }
        }

        val message = update?.message
        if (message?.text == null) {
            return
        }

        if (!lastAuthorRegex.containsMatchIn(message.text) && botUtils.isMessageInChat(message)) {
            botConfig.lastAuthor = message.from
        }

        botConfig.pignolo = nextInt(100) > 90

        actions.forEach {
            thread(start = true, name = it::class.simpleName) {
                it.doAction(message)
            }
        }
    }


}
