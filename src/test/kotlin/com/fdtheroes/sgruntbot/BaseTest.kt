package com.fdtheroes.sgruntbot

import com.fasterxml.jackson.databind.ObjectMapper
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.*
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.message.Message

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
            User(it.arguments.first() as Long, "", false).apply {
                this.userName = "Username_$id"
            }
        }.whenever(botUtils).getChatMember(any())

        doAnswer {
            val actionResponse = it.arguments.first() as ActionResponse
            _actionResponses.add(actionResponse)
        }.whenever(botUtils).rispondi(any(), any())

        doAnswer {
            val actionResponse = it.arguments.first() as ActionResponse
            _actionResponses.add(actionResponse)
        }.whenever(botUtils).messaggio(any())
    }

    fun message(
        text: String,
        from: User = user(),
        replyToMessage: Message? = null,
    ): Message {
        return Message().apply {
            this.text = text
            this.chat = Chat(botConfig.chatId.toLong(), "group")
            this.from = from
            this.replyToMessage = replyToMessage
        }
    }

    fun user(id: Long = 42, userName: String = "Pippo", firstName: String = ""): User {
        return User(id, firstName, false).apply {
            this.userName = userName
        }
    }

    fun user(user: Users): User {
        return User(user.id, user.name.lowercase(), false).apply {
            this.userName = user.name
        }
    }

}
