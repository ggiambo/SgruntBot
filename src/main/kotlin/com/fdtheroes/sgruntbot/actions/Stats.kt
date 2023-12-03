package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.models.Stats
import com.fdtheroes.sgruntbot.actions.persistence.StatsService
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.ChartUtils
import com.fdtheroes.sgruntbot.utils.StatsUtil
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.InputFile

@Lazy
@Service
class Stats(
    private val statsService: StatsService,
    private val statsUtil: StatsUtil,
    private val botUtils: BotUtils,
) : Action, HasHalp {

    private val regex = Regex("^!stats(.*)\$", RegexOption.IGNORE_CASE)

    override fun doAction(ctx: ActionContext) {
        if (!botUtils.isMessageInChat(ctx.message)) {
            return
        }

        if (ctx.message.from.isBot) {
            return
        }

        statsService.increaseStats(ctx.message.from.id)

        if (regex.matches(ctx.message.text)) {
            val tipo = StatsUtil.StatsType.getByType(regex.find(ctx.message.text)?.groupValues?.get(1))
            val inputFile = statsUtil.getStats(tipo, ctx.getChatMember)
            ctx.addResponse(ActionResponse.photo("", inputFile))
        }
    }

    override fun halp() = """
        <b>!stats <i>tipo</i></b> le stats dei logorroici, dove <i>tipo</i>
        <b>g</b> stats di questa giornata
        <b>s</b> stats di questa settimana
        <b>m</b> stats di questo mese
        <b>a</b> stats di quest'anno
        """.trimIndent()

}