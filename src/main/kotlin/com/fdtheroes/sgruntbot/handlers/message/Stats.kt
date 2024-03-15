package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.StatsService
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.StatsUtil
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Stats(
    botUtils: BotUtils,
    botConfig: BotConfig,
    private val statsService: StatsService,
    private val statsUtil: StatsUtil,
) : MessageHandler(botUtils, botConfig), HasHalp {

    private val regex = Regex("^!stats(.*)\$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        if (!botUtils.isMessageInChat(message)) {
            return
        }

        if (message.from.isBot) {
            return
        }

        statsService.increaseStats(message.from.id)

        if (regex.matches(message.text)) {
            val tipo = StatsUtil.StatsType.getByType(regex.find(message.text)?.groupValues?.get(1))
            val inputFile = statsUtil.getStats(tipo)
            botUtils.rispondi(ActionResponse.photo("", inputFile), message)
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