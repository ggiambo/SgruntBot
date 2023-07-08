package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.actions.Action
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import java.time.LocalDateTime
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

@Service
class Bot(
    private val botConfig: BotConfig,
    private val botUtils: BotUtils,
    private val actions: List<Action>,
) : TelegramLongPollingBot(botConfig.defaultBotOptions, botConfig.token) {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val lastAuthorRegex = Regex("^!last\$", RegexOption.IGNORE_CASE)
    val coroutineScope = CoroutineScope(SupervisorJob())

    @PostConstruct
    fun postConstruct() {
        TelegramBotsApi(DefaultBotSession::class.java).registerBot(this)
        log.info("Sono partito!")
    }

    override fun getBotUsername(): String {
        return botConfig.botName
    }

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

        coroutineScope.launch {
            val ctx = ActionContext(message, this@Bot::getChatMember)
            actions.forEach { it.doAction(ctx) }

            ctx.actionResponses.forEach { rispondi(it, message) }
        }
    }

    fun rispondi(actionMessage: ActionResponse, message: Message): Unit {
        when (actionMessage.type) {
            ActionResponseType.Message -> rispondiMessaggio(message, actionMessage.message!!)
            ActionResponseType.Photo -> rispondiPhoto(message, actionMessage.message!!, actionMessage.inputFile!!)
            ActionResponseType.Audio -> rispondiAudio(message, actionMessage.inputFile!!)
        }
    }

    fun messaggio(actionMessage: ActionResponse) {
        when (actionMessage.type) {
            ActionResponseType.Message -> messaggio(actionMessage.message!!)
            ActionResponseType.Photo -> photo(actionMessage.inputFile!!)
            ActionResponseType.Audio -> audio(actionMessage.inputFile!!)
        }
    }

    private fun sgruntyScrive(chatId: Long, actionType: ActionType = ActionType.TYPING) {
        execute(
            SendChatAction().apply {
                this.setChatId(chatId)
                this.setAction(actionType)
            }
        )
        sleep(1..3)
    }

    private fun messaggio(textmd: String) {
        execute(
            SendMessage().apply {
                this.chatId = botConfig.chatId
                this.parseMode = ParseMode.HTML
                this.text = textmd
            }
        )
    }

    private fun rispondiMessaggio(message: Message, textmd: String) {
        sgruntyScrive(message.chatId)
        execute(
            SendMessage().apply {
                this.chatId = message.chat.id.toString()
                this.replyToMessageId = message.messageId
                this.parseMode = ParseMode.HTML
                this.text = textmd
            }
        )
    }

    private fun photo(photo: InputFile) {
        execute(
            SendPhoto().apply {
                this.chatId = botConfig.chatId
                this.parseMode = ParseMode.HTML
                this.photo = photo
            }
        )
    }

    private fun rispondiPhoto(message: Message, caption: String, photo: InputFile) {
        sgruntyScrive(message.chatId, ActionType.UPLOADDOCUMENT)
        execute(
            SendPhoto().apply {
                this.chatId = message.chat.id.toString()
                this.replyToMessageId = message.messageId
                this.parseMode = ParseMode.HTML
                this.photo = photo
                this.caption = caption
            }
        )
    }

    private fun audio(audio: InputFile) {
        execute(
            SendAudio().apply {
                this.chatId = botConfig.chatId
                this.audio = audio
            }
        )
    }

    private fun rispondiAudio(message: Message, audio: InputFile) {
        sgruntyScrive(message.chatId, ActionType.UPLOADDOCUMENT)
        execute(
            SendAudio().apply {
                this.chatId = message.chat.id.toString()
                this.replyToMessageId = message.messageId
                this.audio = audio
            }
        )
    }

    @Cacheable(cacheNames = ["userName"])
    fun getChatMember(userId: Long): User? {
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
