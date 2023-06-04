package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Parolacce(private val botUtils: BotUtils, private val botConfig: BotConfig) : Action {

    private val regex = Regex(
        "[ck]a[tz]z[io]|[ck]ulo|\\bfica\\b|vaffanculo|stronz[aoie]|coglion[aei]|merda",
        RegexOption.IGNORE_CASE
    )

    override fun doAction(message: Message, doNext: (ActionResponse) -> Unit) {
        if (botConfig.pignolo && regex.containsMatchIn(message.text)) {
            val userLink = botUtils.getUserLink(message.from)
            doNext(ActionResponse.message(getTesto(userLink)))
        }
    }

    private fun getTesto(userLink: String): String {
        return listOf(
            "$userLink non approvo il tuo linguaggio, tuttavia in uno sforzo congiunto nella direzione del benessere comune non sarò io a dirti cosa dire o meno, ma storcerò soltanto il naso.",
            "$userLink non dire parolacce!",
            "Ma dai $userLink, ci sono dei bambini!",
            "Cosa mi tocca sentire! $userLink, pregherò per la tua anima",
            "$userLink vai subito a lavarti la bocca col sapone!",
            "Gnbe gne $userLink specchio riflesso!",
            "Un giovanotto come $userLink che dice queste parole?!",
        ).random()
    }
}
