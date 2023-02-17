package com.fdtheroes.sgruntbot

import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.meta.api.methods.ActionType
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import java.io.Serializable
import java.util.concurrent.CompletableFuture
import kotlin.random.Random

@Service
class SgruntBot(private val botConfig: BotConfig) : DefaultAbsSender(botConfig.defaultBotOptions, botConfig.token) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun <T : Serializable, M : BotApiMethod<T>> rispondi(message: M): CompletableFuture<T>? {
        return executeAsync(message)
    }

    fun rispondi(sendAudio: SendAudio): CompletableFuture<Message> {
        return executeAsync(sendAudio)
    }

    fun rispondi(sendPhoto: SendPhoto): CompletableFuture<Message> {
        return executeAsync(sendPhoto)
    }

    fun rispondiAsText(message: Message, text: String) {
        sleep(0..5)
        val reply = SendMessage()
        reply.chatId = message.chatId.toString()
        reply.text = text
        reply.replyToMessageId = message.messageId
        rispondi(reply)
    }

    fun rispondi(message: Message, textmd: String) {
        val sendChatAction = SendChatAction()
        sendChatAction.setChatId(message.chatId)
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