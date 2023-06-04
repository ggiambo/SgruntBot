package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.actions.Action
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.ActionResponseType
import jakarta.annotation.PostConstruct
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

        val actionsIterator = actions.iterator()
        val responses = mutableListOf<ActionResponse>()

        lateinit var doNext: (ActionResponse) -> Unit
        doNext = { resp: ActionResponse ->
            responses.add(resp)
            if (actionsIterator.hasNext()) actionsIterator.next().doAction(message, doNext)
        }

        val ctx = ActionContext(message, this::getChatMember)
        actionsIterator.next().doAction(message, doNext)

        responses.forEach {
            when (it.type) {
                ActionResponseType.Message -> rispondiMessaggio(message, it.message!!)
                ActionResponseType.Photo -> rispondiPhoto(message, it.inputFile!!)
                ActionResponseType.Audio -> rispondiAudio(message, it.inputFile!!)

            }
        }
    }

    private fun sgruntyScrive(message: Message, actionType: ActionType = ActionType.TYPING) {
        execute(
            SendChatAction().apply {
                this.setChatId(message.chatId)
                this.setAction(actionType)
            }
        )
        sleep(3..3)
    }

    fun rispondiMessaggio(message: Message, textmd: String) {
        sgruntyScrive(message)
        execute(
            SendMessage().apply {
                this.chatId = message.chat.id.toString()
                this.replyToMessageId = message.messageId
                this.parseMode = ParseMode.HTML
                this.text = textmd
            }
        )
    }

    private fun rispondiPhoto(message: Message, photo: InputFile) {
        sgruntyScrive(message, ActionType.UPLOADDOCUMENT)
        execute(
            SendPhoto().apply {
                this.chatId = message.chat.id.toString()
                this.replyToMessageId = message.messageId
                this.parseMode = ParseMode.HTML
                this.photo = photo
            }
        )
    }

    private fun rispondiAudio(message: Message, audio: InputFile) {
        sgruntyScrive(message, ActionType.UPLOADDOCUMENT)
        execute(
            SendAudio().apply {
                this.chatId = message.chat.id.toString()
                this.replyToMessageId = message.messageId
                this.audio = audio
            })
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
