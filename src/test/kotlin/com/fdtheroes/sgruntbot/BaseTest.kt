package com.fdtheroes.sgruntbot

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.isA
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.springframework.core.env.Environment
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import java.io.Serializable
import java.util.concurrent.CompletableFuture

open class BaseTest {

    val botArguments = mutableListOf<Any>()

    @BeforeEach
    fun resetContext() = Context.reset()

    val isLocalProxy = System.getenv()["SPRING_ACTIVE_PROFILE"] == "local-proxy"

    val botConfig: BotConfig = mock {
        on { chatId } doAnswer { "-9999" }
        on { defaultBotOptions } doAnswer {
            DefaultBotOptions()
                .apply {
                    if (isLocalProxy) {
                        this.proxyType = DefaultBotOptions.ProxyType.HTTP
                        this.proxyHost = "127.0.0.1"
                        this.proxyPort = 8888
                    }
                }

        }
    }
    val botUtils = BotUtils(botConfig)
    val mapper = ObjectMapper()

    val sgruntBot: Bot = spy(Bot(botConfig, emptyList())) {
        onGeneric { rispondi(isA<BotApiMethod<Serializable>>()) } doAnswer {
            botArguments.add(it.arguments.first())
            CompletableFuture.completedFuture(message("done"))
        }
        onGeneric { rispondi(isA<SendAudio>()) } doAnswer {
            botArguments.add(it.arguments.first())
            CompletableFuture.completedFuture(message("done"))
        }
        onGeneric { rispondi(isA<SendPhoto>()) } doAnswer {
            botArguments.add(it.arguments.first())
            CompletableFuture.completedFuture(message("done"))
        }
        onGeneric { sleep(isA()) } doAnswer { }
    }

    fun message(
        text: String,
        from: User = user(),
        replyToMessage: Message? = null
    ): Message {
        return Message().apply {
            this.text = text
            this.chat = Chat().apply { this.id = botConfig.chatId.toLong() }
            this.from = from
            this.replyToMessage = replyToMessage
        }
    }

    fun user(id: Long = 42, userName: String = "Pippo", firstName: String = ""): User {
        return User().apply {
            this.id = id
            this.userName = userName
            this.firstName = firstName
        }
    }

    fun user(user: Users): User {
        return User().apply {
            this.id = user.id
            this.userName = user.name
            this.firstName = user.name.lowercase()
        }
    }

}
