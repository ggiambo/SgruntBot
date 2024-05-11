package com.fdtheroes.sgruntbot

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.methods.updates.AllowedUpdates
import org.telegram.telegrambots.meta.api.objects.User
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URI
import java.time.LocalDateTime

@Service
class BotConfig(
    @Value("\${CHAT_ID}") val chatId: String,
    @Value("\${TELEGRAM_TOKEN}") val telegramToken: String,
    @Value("\${IMGUR_CLIENT_ID}") val imgurClientId: String,
) {

    val botName = "SgruntBot"
    val defaultBotOptions by lazy { initDefaultBotOptions() }
    val proxy by lazy { initProxy(defaultBotOptions) }

    var lastSuper: User? = null
    var lastAuthor: User? = null
    var pignolo: Boolean = false
    var pausedTime: LocalDateTime? = null

    private fun initDefaultBotOptions(): DefaultBotOptions {
        val defaultBotOptions = DefaultBotOptions()
        defaultBotOptions.allowedUpdates = listOf(
            AllowedUpdates.MESSAGE,
            "message_reaction",
        )

        val proxy = System.getenv()["https_proxy"]
        if (proxy.isNullOrEmpty()) {
            return defaultBotOptions
        }
        val uri = URI(proxy)
        defaultBotOptions.proxyType = DefaultBotOptions.ProxyType.HTTP
        defaultBotOptions.proxyHost = uri.host
        defaultBotOptions.proxyPort = uri.port

        return defaultBotOptions
    }

    private fun initProxy(options: DefaultBotOptions): Proxy {
        if (options.proxyType == DefaultBotOptions.ProxyType.NO_PROXY) {
            return Proxy.NO_PROXY
        }
        return Proxy(Proxy.Type.HTTP, InetSocketAddress(options.proxyHost, options.proxyPort))
    }

    fun reset() {
        lastSuper = null
        lastAuthor = null
        pignolo = false
        pausedTime = null
    }

}
