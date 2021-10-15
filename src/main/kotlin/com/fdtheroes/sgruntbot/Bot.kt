package com.fdtheroes.sgruntbot

import com.fdtheroes.sgruntbot.actions.Action
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.reflections.Reflections
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.random.Random

open class Bot(private val botToken: String, private val botUsername: String) : TelegramLongPollingBot() {

    private val actions: List<Action>
    private val context = Context()
    private val lastAuthorRegex = Regex("^!last\$", RegexOption.IGNORE_CASE)

    init {
        BotUtils.init(this)
        execute(SendMessage(BotUtils.instance.chatId, "Sono partito"))
        actions = Reflections("com.fdtheroes.sgruntbot")
            .getSubTypesOf(Action::class.java)
            .map { it.getDeclaredConstructor().newInstance() }
    }

    override fun getBotToken(): String {
        return botToken
    }

    override fun getBotUsername(): String {
        return botUsername
    }

    override fun onUpdateReceived(update: Update?) {
        if (context.pausedTime != null) {
            if (ChronoUnit.MINUTES.between(context.pausedTime, LocalDateTime.now()) > 5) {
                context.pausedTime = null
            } else {
                return
            }
        }

        val message = update?.message
        if (message == null) {
            return
        }

        if (!lastAuthorRegex.containsMatchIn(message.text)) {
            if (message.from.userName != null) {
                context.lastAuthor = message.from.userName
            } else {
                context.lastAuthor = message.from.firstName
            }
        }

        context.pignolo = Random.nextInt(100) > 90

        actions.forEach {
            it.doAction(message, context)
        }
    }

}