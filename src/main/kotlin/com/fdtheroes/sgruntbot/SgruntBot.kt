package com.fdtheroes.sgruntbot

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import java.io.Serializable
import java.util.concurrent.CompletableFuture

interface SgruntBot {

    fun rispondiAsText(message: Message, text: String)

    fun rispondi(message: Message, textmd: String)

    fun <T : Serializable, M : BotApiMethod<T>> rispondi(message: M): CompletableFuture<T>?

    fun rispondi(sendAudio: SendAudio): CompletableFuture<Message>

    fun rispondi(sendPhoto: SendPhoto): CompletableFuture<Message>

    fun getChatMember(userId: Long): User?

}
