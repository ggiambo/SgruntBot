package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.random.Random.Default.nextInt

@Service
class Buonanotte(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    private val buonanotte = listOf(
        "Hai visto che ore sono? Buonanotte!",
        "Ormai è tardi, buonanotte!",
        "Devresti riposare, buonanotte!",
        "Sei ancora sveglio a quest'ora? Buonanotte!",
        "Invece che perdere tempo qui, vai a dormire, buonanotte!",
        "Vai a farti una bella dormita, buonanotte!",
        "Non hai sonno? Buonanotte!",
    )

    override fun handle(message: Message) {
        if (nextInt(100) == 0) {
            val now = LocalDateTime.now()
            if (now > from() && now < to()) {
                botUtils.rispondi(ActionResponse.message(buonanotte.random()), message)
            }
        }
    }

    // le 10 di sera
    private fun from() = LocalDate.now().atTime(22, 0)

    // le 4 di domani mattina
    private fun to() = LocalDate.now().plusDays(1).atTime(4, 0)

}