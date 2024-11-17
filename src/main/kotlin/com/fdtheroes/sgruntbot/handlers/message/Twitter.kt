package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import kotlin.random.Random.Default.nextInt

@Service
class Twitter(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    private val risposte = listOf(
        "In questa chat non nominiamo di Twitter",
        "Basta Elon/Twitter Spam!",
        "Twitter? Shitter!",
        "Una persona perbene come te che va a ravanare in Twitter?",
        "A Sgrunty non piace Twitter",
    )

    private val regex = Regex("https://twitter.com/")

    override fun handle(message: Message) {
        if (nextInt(5) == 0 && regex.containsMatchIn(message.text)) {
            botUtils.rispondi(ActionResponse.message(risposte.random()), message)
        }
    }
}