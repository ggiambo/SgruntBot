package com.fdtheroes.sgruntbot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User

open class BaseTest {

    @BeforeEach
    fun resetContext() = botConfig.reset()

    val botConfig: BotConfig = BotConfig(
        chatId = "-9999",
        telegramTokenFile = "dummyToken.txt",
        imgUrClientIdFile = "dummyToken.txt",
    )

    val botUtils = BotUtils(botConfig)
    val mapper = ObjectMapper()

    val sgruntBot: Bot = mock {
        onGeneric { sleep(isA()) } doAnswer { }
    }

    // per qualche oscura ragione, dev'essere qui e non in "spy" (Il metodo originale viene chiamato!)
    init {
        doAnswer {
            User().apply {
                val id = it.arguments.first() as Long
                this.id = id
                this.userName = "Username_$id"
            }
        }.whenever(sgruntBot).getChatMember(any())
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

    fun actionContext(
        text: String,
        from: User = user(),
        replyToMessage: Message? = null
    ): ActionContext {
        return ActionContext(message(text, from, replyToMessage), sgruntBot::getChatMember)
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
