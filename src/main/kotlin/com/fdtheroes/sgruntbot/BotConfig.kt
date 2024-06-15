package com.fdtheroes.sgruntbot

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator
import org.telegram.telegrambots.meta.TelegramUrl
import org.telegram.telegrambots.meta.api.methods.updates.AllowedUpdates
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates
import org.telegram.telegrambots.meta.api.objects.User
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URI
import java.time.LocalDateTime


@Configuration
class BotConfig(
    @Value("\${CHAT_ID}") val chatId: String,
    @Value("\${TELEGRAM_TOKEN}") val telegramToken: String,
    @Value("\${IMGUR_CLIENT_ID}") val imgurClientId: String,
) {

    val botName = "SgruntBot"
    val proxy by lazy { initProxy() }
    val allowedUpdates = DefaultGetUpdatesGenerator(listOf(AllowedUpdates.MESSAGE, "message_reaction"))
    val defaultUrl = { TelegramUrl.DEFAULT_URL }

    var lastSuper: User? = null
    var lastAuthor: User? = null
    var pignolo: Boolean = false
    var pausedTime: LocalDateTime? = null

    private fun initProxy(): Proxy {
        val proxyEnv = System.getenv()["https_proxy"] ?: System.getenv()["http_proxy"]
        if (proxyEnv.isNullOrEmpty()) {
            return Proxy.NO_PROXY
        }
        val uri = URI(proxyEnv)
        return Proxy(Proxy.Type.HTTP, InetSocketAddress(uri.host, uri.port))
    }

    fun reset() {
        lastSuper = null
        lastAuthor = null
        pignolo = false
        pausedTime = null
    }

}
