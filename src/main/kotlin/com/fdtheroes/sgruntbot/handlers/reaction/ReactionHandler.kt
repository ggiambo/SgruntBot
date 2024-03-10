package com.fdtheroes.sgruntbot.handlers.reaction

import com.fdtheroes.sgruntbot.handlers.Handler
import com.fdtheroes.sgruntbot.persistence.KarmaService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.reactions.ReactionTypeEmoji

@Service
class ReactionHandler(private val karmaService: KarmaService) : Handler {

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
        if (update?.messageReaction == null) {
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
            karmaService.takeGiveKarma(user.id) { it + 1 }
        }
    }

}