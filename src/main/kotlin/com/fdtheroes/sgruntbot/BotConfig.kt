package com.fdtheroes.sgruntbot

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.telegram.telegrambots.longpolling.util.DefaultGetUpdatesGenerator
import org.telegram.telegrambots.meta.TelegramUrl
import org.telegram.telegrambots.meta.api.methods.updates.AllowedUpdates
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.SocketAddress
import java.net.URI
import java.time.LocalDateTime

@Component
class BotConfig(
    @param:Value($$"${CHAT_ID}") val chatId: String,
    @param:Value($$"${TELEGRAM_TOKEN}") val telegramToken: String,
    @param:Value($$"${IMGUR_CLIENT_ID}") val imgurClientId: String,
) {

    val botName = "SgruntBot"
    val proxy by lazy { initProxy() }
    val suoraProxyAddress = InetSocketAddress("198.98.49.55", 8118)
    val suoraProxy = Proxy(Proxy.Type.HTTP, suoraProxyAddress)
    val allowedUpdates = DefaultGetUpdatesGenerator(listOf(AllowedUpdates.MESSAGE, AllowedUpdates.MESSAGEREACTION))
    val defaultUrl = { TelegramUrl.DEFAULT_URL }

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

    // usato solo nei test!
    fun reset() {
        pignolo = false
        pausedTime = null
    }

    @Bean
    fun nowSupplier(): () -> LocalDateTime = { LocalDateTime.now() }

}
