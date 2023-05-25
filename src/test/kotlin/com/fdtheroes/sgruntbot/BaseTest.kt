package com.fdtheroes.sgruntbot

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.isA
import org.mockito.kotlin.spy
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
    fun resetContext() = botConfig.reset()

    val isLocalProxy = System.getenv()["SPRING_ACTIVE_PROFILE"] == "local-proxy"

    val botConfig: BotConfig = BotConfig(
            chatId = "-9999",
            telegramTokenFile = "dummyToken.txt",
            imgUrClientIdFile = "dummyToken.txt",
            proxy = if (isLocalProxy) "http://127.0.0.1:8888" else ""
    )

    val botUtils = BotUtils(botConfig)
    val mapper = ObjectMapper()

    val sgruntBot: Bot = spy(Bot(botConfig, botUtils, emptyList())) {
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
        onGeneric { getChatMember(isA()) } doAnswer {
            User().apply {
                val id = it.arguments.first() as Long
                this.id = id
                this.userName = "Username_$id"
            }
        }
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
            this.isBot = false
        }
    }

    fun user(user: Users): User {
        return User().apply {
            this.id = user.id
            this.userName = user.name
            this.firstName = user.name.lowercase()
            this.isBot = false
        }
    }

}
