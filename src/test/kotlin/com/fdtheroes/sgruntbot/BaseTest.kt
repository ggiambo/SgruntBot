package com.fdtheroes.sgruntbot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User

open class BaseTest {

    private val _actionResponses = mutableListOf<ActionResponse>()
    val actionResponses: List<ActionResponse>
        get() = _actionResponses

    @BeforeEach
    fun resetContext() {
        botConfig.reset()
        _actionResponses.clear()
    }

    val botConfig: BotConfig = BotConfig(
        chatId = "-9999",
        telegramToken = "dummyToken.txt",
        imgurClientId = "dummyToken.txt",
    )

    val botUtils = spy(BotUtils(botConfig))
    val mapper = ObjectMapper()

    // per qualche oscura ragione, dev'essere qui e non in "spy" (Il metodo originale viene chiamato!)
    init {
        doAnswer {
            User().apply {
                val id = it.arguments.first() as Long
                this.id = id
                this.userName = "Username_$id"
            }
        }.whenever(botUtils).getChatMember(any())

        doAnswer {
            User().apply {
                val actionResponse = it.arguments.first() as ActionResponse
                _actionResponses.add(actionResponse)
            }
        }.whenever(botUtils).rispondi(any(), any())

        doAnswer {
            User().apply {
                val actionResponse = it.arguments.first() as ActionResponse
                _actionResponses.add(actionResponse)
            }
        }.whenever(botUtils).messaggio(any())
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
