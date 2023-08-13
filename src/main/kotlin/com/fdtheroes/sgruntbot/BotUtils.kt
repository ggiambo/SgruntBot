package com.fdtheroes.sgruntbot

import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import java.io.InputStream
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.stream.StreamSupport

@Service
class BotUtils(private val botConfig: BotConfig) {

    private val proxy = initProxy(botConfig.defaultBotOptions)

    fun isMessageInChat(message: Message): Boolean {
        return message.chatId?.toString() == botConfig.chatId
    }

    fun getUserName(user: User?): String {
        if (user == null) {
            return ""
        }
        val name: String
        if (!user.userName.isNullOrEmpty()) {
            name = user.userName
        } else {
            name = user.firstName
        }
        return name
    }

    fun getUserLink(user: User?): String {
        if (user == null) {
            return ""
        }
        val name = getUserName(user)
        return """<a href="tg://user?id=${user.id}">${name}</a>"""
    }

    fun streamFromURL(
        url: String,
        params: String? = null,
        headers: List<Pair<String, String>> = emptyList()
    ): InputStream {
        return URL(String.format(url, params))
            .openConnection(proxy)
            .apply { headers.forEach { setRequestProperty(it.first, it.second) } }
            .getInputStream()
    }

    fun textFromURL(url: String, params: String? = null, headers: List<Pair<String, String>> = emptyList()): String {
        return streamFromURL(url, params)
            .readAllBytes()
            .decodeToString()
    }

    private fun initProxy(options: DefaultBotOptions): Proxy {
        if (options.proxyType == DefaultBotOptions.ProxyType.NO_PROXY) {
            return Proxy.NO_PROXY
        }
        return Proxy(Proxy.Type.HTTP, InetSocketAddress(options.proxyHost, options.proxyPort))
    }

    companion object {
        infix fun <T> T.elseIfNull(other: T?): T? {
            if (this != null) {
                return this
            }
            return other
        }

        infix fun <T> T.elseIfNull(other: () -> T?): T? {
            if (this != null) {
                return this
            }
            return other()
        }

        fun String.urlEncode(): String {
            return URLEncoder.encode(this, StandardCharsets.UTF_8)
        }

        fun Iterable<*>?.length(): Long {
            return StreamSupport.stream(this?.spliterator(), false).count()
        }

        fun LocalDate.toDate(): Date {
            return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
        }

        fun LocalDateTime.toDate(): Date {
            return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
        }
    }
}
