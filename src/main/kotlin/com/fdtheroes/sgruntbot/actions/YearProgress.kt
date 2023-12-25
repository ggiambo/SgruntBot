package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class YearProgress : Action, HasHalp {

    private val regex = Regex("^!yp$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        if (regex.containsMatchIn(ctx.message.text)) {
            ctx.addResponse(ActionResponse.message(yearProgression()))
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
