package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import java.time.LocalDateTime

@Service
class YearProgress(botUtils: BotUtils, botConfig: BotConfig) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!yp$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (regex.containsMatchIn(message.text)) {
            botUtils.rispondi(ActionResponse.message(yearProgression()), message)
        }
    }

    override fun halp() = "<b>yp</b> percentuale dell'anno che hai sprecato"

    companion object {
        private const val BAR_LENGTH = 16
        private const val COMPLETED_CHAR = "▓"
        private const val NOT_COMPLETED_CHAR = "░"
        private const val CALENDAR_CHAR = "\uD83D\uDCC5"
        fun yearProgression(): String {
            val percent = LocalDateTime.now().dayOfYear.toFloat() / 365
            val completedPercent = Math.round(percent * 100)
            val completedBar = COMPLETED_CHAR.repeat(Math.round(percent * BAR_LENGTH))
            val notCompletedBar = NOT_COMPLETED_CHAR.repeat(BAR_LENGTH - completedBar.length)
            val progress = "$completedBar$notCompletedBar $completedPercent%"
            return "Year Progress $CALENDAR_CHAR\n$progress"
        }
    }

}
