package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Context
import org.telegram.telegrambots.meta.api.objects.Message
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheOreSono : Action {

    private val regex = Regex("che ore sono|che ora è", setOf(RegexOption.IGNORE_CASE, RegexOption.MULTILINE))
    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    private val ore = listOf(
        "mezzanotte",
        "l'una",
        "le due",
        "le tre",
        "le quattro",
        "le cinque",
        "le sei",
        "le sette",
        "le otto",
        "le nove",
        "le dieci",
        "le undici",
        "mezzogiorno"
    )

    private val minuti = listOf(
        "in punto",
        "e cinque",
        "e dieci",
        "e un quarto",
        "e venti",
        "e venticinque",
        "e mezzo",
        "e trentacinque circa",
        "meno venti",
        "meno un quarto",
        "meno dieci",
        "meno cinque"
    )

    override fun doAction(message: Message, context: Context) {
        if (context.pignolo && regex.containsMatchIn(message.text)) {
            BotUtils.instance.rispondi(message, oreInLettere())
        }
    }

    private fun oreInLettere(): String {
        val now = LocalDateTime.now()
        var h = now.hour
        val m = now.minute / 5
        if (m >= 8) {
            h += 1
        }
        if (h > 12) {
            h -= 12
        }

        return ore[h] + " " + minuti[m] + " (precisamente ${formatter.format(now)}) ok?)"
    }

}