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
import java.time.LocalDateTime
import kotlin.random.Random


@Service
class SgruntBot(
    private val botConfig: BotConfig,
    private val handlers: List<Handler>,
    private val botUtils: BotUtils,
    private val gitUtils: GitUtils,
) : LongPollingSingleThreadUpdateConsumer {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val lastAuthorRegex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    @PostConstruct
    fun postConstruct() {
        TelegramBotsLongPollingApplication().registerBot(
            botConfig.telegramToken,
            botConfig.defaultUrl,
            botConfig.allowedUpdates,
            this
        )
        try {
            publishNewVersionInfo()
        } catch (e: Exception) {
            log.error("Errore durante la pubblicazione delle novità: ${e.message}", e)
        }
        log.info("Sono partito!")
    }

    override fun consume(update: Update) {
        if (!preCheck(update)) {
            return
        }

        handlers.forEach {
            CoroutineScope(Dispatchers.Default).launch { it.handle(update) }
        }
    }

    private fun preCheck(update: Update): Boolean {
        if (botConfig.pausedTime != null) {
            if (LocalDateTime.now() < botConfig.pausedTime) {
                botConfig.pausedTime = null
                log.info("Posso parlare di nuovo!")
            } else {
                return false
            }
        }

        val message = update.message
        if (message == null) {
            return false
        }

        if (message.text.isNullOrEmpty()) {
            return false
        }

        if (!lastAuthorRegex.containsMatchIn(message.text) && botUtils.isMessageInChat(message)) {
            botConfig.lastAuthor = message.from
        }

        botConfig.pignolo = Random.nextInt(100) > 90

        if (botUtils.getChatMember(message.from.id) == null) {
            return false
        }

        return true
    }

    private fun publishNewVersionInfo() {
        val deltaMessages = gitUtils.getDeltaFromLatestDeployment().take(10)
            .filter { it.committer.login != "github-actions[bot]" }
            .map { it.commitShortInfo.message }
            .map { it.split('\n').first() }
            .joinToString(separator = "\n") { "- $it" }

        gitUtils.updateDeployedHash()

        if (deltaMessages.isNotEmpty()) {
            val message = "Sono partito!\nEcco le novità:\n$deltaMessages"
            botUtils.messaggio(ActionResponse.message(message))
        }
    }

}
