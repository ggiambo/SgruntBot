package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.actions.Action
import com.fdtheroes.sgruntbot.actions.HasHalp
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.io.Serializable
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.CompletableFuture
import javax.annotation.PostConstruct
import kotlin.concurrent.thread
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

@Service
class Bot(
    private val botConfig: BotConfig,
    private val actions: List<Action>,
) : TelegramLongPollingBot(botConfig.defaultBotOptions), SgruntBot {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val lastAuthorRegex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    @PostConstruct
    fun postConstruct() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
        log.info("Sono partito!")
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
            rispondi(message, help)
            return
        }

        actions.forEach {
            thread(start = true, name = it.javaClass.simpleName) {
                it.doAction(message, this)
            }
        }
    }

    override fun rispondiAsText(message: Message, text: String) {
        sleep(0..5)
        val reply = SendMessage()
        reply.chatId = message.chatId.toString()
        reply.text = text
        reply.replyToMessageId = message.messageId
        rispondi(reply)
    }

    override fun rispondi(message: Message, textmd: String) {
        val sendChatAction = SendChatAction()
        sendChatAction.chatId = message.chatId.toString()
        sendChatAction.setAction(ActionType.TYPING)
        rispondi(sendChatAction)
        sleep(3..5)
        val reply = SendMessage()
        reply.chatId = message.chat.id.toString()
        reply.replyToMessageId = message.messageId
        reply.parseMode = ParseMode.HTML
        reply.text = textmd
        rispondi(reply)
    }

    override fun <T : Serializable, M : BotApiMethod<T>> rispondi(message: M): CompletableFuture<T>? {
        return executeAsync(message)
    }

    override fun rispondi(sendAudio: SendAudio): CompletableFuture<Message> {
        return executeAsync(sendAudio)
    }

    override fun rispondi(sendPhoto: SendPhoto): CompletableFuture<Message> {
        return executeAsync(sendPhoto)
    }

    override fun getChatMember(userId: Long): User? {
        val getChatMember = GetChatMember().apply {
            this.chatId = botConfig.chatId
            this.userId = userId
        }
        return try {
            execute(getChatMember)?.user
        } catch (e: Exception) {
            log.error("Problema con l'utente $userId", e)
            null
        }
    }

    fun sleep(seconds: IntRange) {
        Thread.sleep(Random.nextLong(seconds.first.toLong() * 1000, seconds.last.toLong() * 1000))
    }

}
