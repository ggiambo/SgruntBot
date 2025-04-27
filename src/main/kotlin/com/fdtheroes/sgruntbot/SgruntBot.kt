package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.handlers.Handler
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.GitUtils
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer
import org.telegram.telegrambots.meta.api.objects.Update


@Service
class SgruntBot(
    private val botConfig: BotConfig,
    private val handlers: List<Handler>,
    private val botUtils: BotUtils,
    private val gitUtils: GitUtils,
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
        publishNewVersionInfo()
        log.info("Sono partito!")
    }

    override fun consume(update: Update) {
        handlers.forEach {
            CoroutineScope(Dispatchers.Default).launch { it.handle(update) }
        }
    }

    private fun publishNewVersionInfo() {
        val deltaMessages = gitUtils.getDeltaFromLatestDeployment()
            .filter { !it.shortMessage.endsWith("[skip actions]") }
            .map { "- ${it.shortMessage}" }
            .joinToString(separator = "\n")

        if (deltaMessages.isNotEmpty()) {
            val message = "Sono partito!\nEcco le novità:\n$deltaMessages"
            botUtils.messaggio(ActionResponse.message(message))
        }

        gitUtils.updateDeployedHash()
    }

}
