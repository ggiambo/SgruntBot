package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message
import java.time.LocalTime
import kotlin.random.Random.Default.nextInt

@Service
class Buonanotte(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig) {

    private val from = LocalTime.of(22, 0)
    private val to = LocalTime.of(4, 0)

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
            val now = LocalTime.now()
            if (now > from && now < to) {
                botUtils.rispondi(ActionResponse.message(buonanotte.random()), message)
            }
        }
    }

}