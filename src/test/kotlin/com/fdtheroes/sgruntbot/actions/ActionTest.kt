package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.Context
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.isA
import org.mockito.kotlin.mock
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendAudio
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import java.io.Serializable

open class ActionTest {

    val botArguments = mutableListOf<Any>()

    @BeforeEach
    fun resetContext() = Context.reset()

    init {
        val bot: Bot = mock {
            on { executeAsync(isA<SendAudio>()) } doAnswer {
                botArguments.add(it.arguments.first())
                null
            }
            on { executeAsync(isA<SendPhoto>()) } doAnswer {
                botArguments.add(it.arguments.first())
                null
            }
            on { executeAsync(isA<BotApiMethod<Serializable>>()) } doAnswer {
                botArguments.add(it.arguments.first())
                null
            }
        }

        BotUtils.init(bot, DefaultBotOptions())
/*
        BotUtils.init(
            bot, DefaultBotOptions()
                .apply {
                    this.proxyType = DefaultBotOptions.ProxyType.HTTP
                    this.proxyHost = "127.0.0.1"
                    this.proxyPort = 8888
                }
        )
*/
    }

    fun message(
        text: String,
        chatId: Long = BotUtils.chatId.toLong(),
        from: User = user(),
        replyToMessage: Message? = null
    ): Message {
        return Message().apply {
            this.text = text
            this.chat = Chat().apply { this.id = chatId }
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

}
