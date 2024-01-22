package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class YearProgress : Action, HasHalp {

    private val regex = Regex("^!yp$", RegexOption.IGNORE_CASE)
    private val barLength = 16
    private val completedChar = "▓"
    private val notCompletedChar = "░"
    private val calendar = "\uD83D\uDCC5"

    override fun doAction(ctx: ActionContext) {
        if (regex.containsMatchIn(ctx.message.text)) {
            val percent = LocalDateTime.now().dayOfYear.toFloat() / 365
            val completedPercent = Math.round(percent * 100)
            val completedBar = completedChar.repeat(Math.round(percent * barLength))
            val notCompletedBar = notCompletedChar.repeat(barLength - completedBar.length)
            val progress = "$completedBar$notCompletedBar $completedPercent%"
            ctx.addResponse(ActionResponse.message("Year Progress $calendar\n$progress"))
        }
    }

    override fun halp() = "<b>yp</b> percentuale dell'anno che hai sprecato"

}
