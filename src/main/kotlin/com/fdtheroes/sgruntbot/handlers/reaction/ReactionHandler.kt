package com.fdtheroes.sgruntbot.handlers.reaction

import com.fdtheroes.sgruntbot.handlers.Handler
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Disabled
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.chat.Chat
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.message.Message
import org.telegram.telegrambots.meta.api.objects.reactions.MessageReactionUpdated
import org.telegram.telegrambots.meta.api.objects.reactions.ReactionTypeEmoji
import kotlin.random.Random.Default.nextInt

@Service
@Disabled
class ReactionHandler(private val botUtils: BotUtils) : Handler {

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
            chat = Chat(reactionUpdated.chat.id, "group")
            this.messageId = reactionUpdated.messageId
        }
    }
}
