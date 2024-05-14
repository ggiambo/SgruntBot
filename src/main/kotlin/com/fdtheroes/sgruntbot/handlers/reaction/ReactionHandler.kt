package com.fdtheroes.sgruntbot.handlers.reaction

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.handlers.Handler
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Chat
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.reactions.MessageReactionUpdated
import org.telegram.telegrambots.meta.api.objects.reactions.ReactionTypeEmoji
import kotlin.random.Random.Default.nextInt

@Service
class ReactionHandler(
    private val botUtils: BotUtils,
    private val botConfig: BotConfig,
) : Handler {

    override suspend fun handle(update: Update) {
        if (update.messageReaction == null) {
            return
        }

        val reactionType = update.messageReaction.newReaction
            .filterIsInstance<ReactionTypeEmoji>()
            .firstOrNull()
        if (reactionType != null && nextInt(5) == 0) { // 20% di probabilità
            val message = messaggio(update.messageReaction)
            botUtils.rispondi(ActionResponse.message(reactionType.emoji), message)
        }
    }

    // dobbiamo costruirci noi il messaggio da soli >:( !
    private fun messaggio(reactionUpdated: MessageReactionUpdated): Message {
        return Message().apply {
            chat = Chat().apply {
                id = reactionUpdated.chat.id
            }
            this.messageId = reactionUpdated.messageId
        }
    }
}
