package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.actions.Action
import com.fdtheroes.sgruntbot.actions.Fortune
import com.fdtheroes.sgruntbot.actions.HasHalp
import com.fdtheroes.sgruntbot.actions.Slogan
import com.fdtheroes.sgruntbot.actions.persistence.KarmaRepository
import com.fdtheroes.sgruntbot.scheduled.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.concurrent.thread
import kotlin.random.Random.Default.nextInt

@Service
class Bot(
    private val botConfig: BotConfig,
    private val karmaRepository: KarmaRepository, // eliminare, sbagliata qui
private val actions: List<Action>
    ) : TelegramLongPollingBot(botConfig.defaultBotOptions) {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val lastAuthorRegex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    init {
        BotUtils.init(this)
        initScheduled()
        log.info("Sono partito!")
    }

    private fun initScheduled() {
        RandomFortune(
            sendMessage = this::executeAsync,
            getFortuneText = Fortune::getFortune
        ).start()
        RandomSlogan(
            sendMessage = this::executeAsync,
            getSloganText = Slogan::fetchSlogan
        ).start()
        RandomImgur(
            sendMessage = this::executeAsync,
            imgurClientId = botConfig.imgurClientId
        ).start()
        RandomCuloDiPapa(
            sendMessage = this::executeAsync,
        ).start()
        ScheduledKarma(karmaRepository).start()
    }

    override fun getBotToken(): String {
        return botConfig.token
    }

    override fun getBotUsername(): String {
        return botConfig.botName
    }

    override fun onUpdateReceived(update: Update?) {
        if (Context.pausedTime != null) {
            if (ChronoUnit.MINUTES.between(Context.pausedTime, LocalDateTime.now()) > 5) {
                Context.pausedTime = null
                log.info("Posso parlare di nuovo!")
            } else {
                return
            }
        }

        val message = update?.message
        if (message?.text == null) {
            return
        }

        if (!lastAuthorRegex.containsMatchIn(message.text)) {
            Context.lastAuthor = message.from
        }

        Context.pignolo = nextInt(100) > 90

        if (message.text == "!help") {
            val help = actions.filterIsInstance<HasHalp>()
                .sortedBy { it.javaClass.simpleName }
                .joinToString("\n") { it.halp() }
            BotUtils.rispondi(message, help)
            return
        }

        actions.forEach {
            thread(start = true, name = it.javaClass.simpleName) {
                it.doAction(message)
            }
        }
    }

}
