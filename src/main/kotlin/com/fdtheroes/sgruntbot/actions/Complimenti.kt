package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import com.fdtheroes.sgruntbot.actions.persistence.ComplimentoService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Complimenti(private val complimentoService: ComplimentoService) : Action, HasHalp {


    private val regex = Regex("^!complimento( .+)*\$", RegexOption.IGNORE_CASE)
    override fun doAction(message: Message, sgruntBot: SgruntBot) {
        if (regex.containsMatchIn(message.text)) {
            val groupValues = regex.find(message.text)!!.groupValues
            val nuovoComplimento = groupValues.getOrNull(1).orEmpty().trim()
            if (nuovoComplimento.isNotEmpty()) {
                complimentoService.saveOrUpdate(message.from.id, nuovoComplimento)
                sgruntBot.rispondiAsText(message, "Ogni tanto ti dirò '${nuovoComplimento}' <3")
            } else {
                val complimento = complimentoService.get(message.from.id)
                if (complimento.isNullOrEmpty()) {
                    sgruntBot.rispondiAsText(message, "Usa '!complimento unComplimento' per innalzare l'autostima.")
                } else {
                    sgruntBot.rispondiAsText(message, "Ogni tanto ti dirò '${complimento}' <3")
                }
            }
        }
    }

    override fun halp() = "<b>!complimento</b> <i>il tuo complimento</i> per avere un complimento ogni tanto."
}