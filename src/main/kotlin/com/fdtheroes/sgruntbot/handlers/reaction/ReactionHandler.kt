package com.fdtheroes.sgruntbot.handlers.reaction

import com.fdtheroes.sgruntbot.handlers.Handler
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.KarmaService
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.reactions.ReactionTypeEmoji
import kotlin.random.Random.Default.nextInt

//@Service
class ReactionHandler(private val karmaService: KarmaService, private val botUtils: BotUtils) : Handler {

    val emojis_plus = setOf(
        "👍",
        "❤",
        "🔥",
        "🥰",
        "👏",
        "😁",
        "🤔",
        "🤯",
        "😱",
        "🎉",
        "🤩",
        "🙏",
        "👌",
        "🕊",
        "🥴",
        "😍",
        "🐳",
        "❤‍🔥",
        "🌚",
        "🌭",
        "💯",
        "🤣",
        "⚡",
        "🍌",
        "🏆",
        "🍓",
        "🍾",
        "💋",
        "😈",
        "🤓",
        "👻",
        "👨‍💻",
        "👀",
        "🎃",
        "🙈",
        "😇",
        "😨",
        "🤝",
        "✍",
        "🤗",
        "🫡",
        "🎅",
        "🎄",
        "☃",
        "💅",
        "🤪",
        "🗿",
        "🆒",
        "💘",
        "🙉",
        "🦄",
        "😘",
        "💊",
        "🙊",
        "😎",
        "👾",
        "🤷‍♂",
        "🤷",
        "🤷‍♀",
        "😡"
    )

    override suspend fun handle(update: Update) {
        if (update.messageReaction == null) {
            return
        }

        val event = update.messageReaction
        event.newReaction.forEach {
            if (it is ReactionTypeEmoji) {
                handleEmoji(it, event.user)
            }
        }
    }

    private fun handleEmoji(emoji: ReactionTypeEmoji, user: User) {
        if (emojis_plus.contains(emoji.emoji)) {
            if (nextInt(10) == 0) { // 10% di probabilità
                karmaService.takeGiveKarma(user.id) { it + 1 }
                val karma = karmaService.getKarma(user.id)
                val messaggio = "La tua buona azione ti ha fatto vincere un Karma, sei a quota ${karma.karma}"
                botUtils.messaggio(ActionResponse.message(messaggio))
            }
        }
    }

}
