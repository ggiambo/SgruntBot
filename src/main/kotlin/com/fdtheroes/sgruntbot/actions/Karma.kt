package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt

@Service
class Karma(
    private val botUtils: BotUtils,
    private val karmaService: KarmaService,
) : Action, HasHalp {

    override fun doAction(ctx: ActionContext) {
        val ricevente = ctx.message.replyToMessage?.from?.id
        if (Regex("\\++").matches(ctx.message.text) && ricevente != null) {
            giveTakeKarma(ctx, ricevente, ctx.message.text.length, Int::inc)
        }
        if (Regex("-+").matches(ctx.message.text) && ricevente != null) {
            if (nextBoolean()) { // 50%
                ctx.addResponse(ActionResponse.message("L'amore vince sempre sull'odio, Sgrunty trasforma il karma negativo in positivo"))
                giveTakeKarma(ctx, ricevente, ctx.message.text.length, Int::inc)
            } else {
                giveTakeKarma(ctx, ricevente, ctx.message.text.length, Int::dec)
            }
        }
        if (ctx.message.text == "!karma") {
            ctx.addResponse(ActionResponse.message(testoKarmaReport(ctx)))
        }
    }

    override fun halp() = """
        <b>!karma</b> mostra la situazione del Karma
        <b>+</b> da un punto karma all'autore del messaggio
        <b>-</b> toglie punto karma all'autore del messaggio
        """.trimIndent()

    private fun giveTakeKarma(
        ctx: ActionContext,
        ricevente: Long,
        n: Int,
        newKarma: (oldKarma: Int) -> Int
    ) {
        val donatore = ctx.message.from.id
        if (donatore == ricevente) {
            ctx.addResponse(ActionResponse.message("Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso"))
            return
        }

        karmaService.precheck(donatore)
        karmaService.precheck(ricevente)

        if (karmaService.getKarmaCredit(donatore) < 1) {
            ctx.addResponse(ActionResponse.message("Hai terminato i crediti per oggi"))
            return
        }

        var wonKarma = 0
        var wonCredit = 0

        for (i in 1..n) {
            if (karmaService.getKarmaCredit(donatore) < 1) {
                break
            }

            karmaService.takeGiveKarma(donatore, ricevente, newKarma)

            if (nextInt(5) == 0) { // 20%
                karmaRoulette(ctx.message, newKarma)
                wonKarma = newKarma(wonKarma)
            }

            if (nextInt(5) == 0) { // 20%
                creditRoulette(ctx.message)
                wonCredit++
            }
        }

        val riceventeLink = botUtils.getUserLink(ctx.message.replyToMessage.from)
        val donatoreLink = botUtils.getUserLink(ctx.message.from)
        val newKarmaRicevente = karmaService.getKarma(ricevente)
        val crediti = karmaService.getKarmaCredit(donatore)
        var karmaMessage = "Karma totale di $riceventeLink: $newKarmaRicevente\nCrediti di $donatoreLink: $crediti"

        if (wonKarma != 0) {
            val newKarmaDonatore = karmaService.getKarma(donatore)
            karmaMessage = karmaMessage.plus("\n\n<b>Karmaroulette</b> ! Hai vinto $wonKarma karma, e ora sei a quota $newKarmaDonatore")
        }

        if (wonCredit == 1) {
            karmaMessage = karmaMessage.plus("\n\n<b>Creditroulette</b> ! Hai vinto un credito")
        } else if (wonCredit > 0) {
            karmaMessage = karmaMessage.plus("\n\n<b>Creditroulette</b> ! Hai vinto $wonCredit crediti")
        }

        ctx.addResponse(ActionResponse.message(karmaMessage))
    }

    private fun karmaRoulette(message: Message, newKarma: (oldKarma: Int) -> Int) {
        val ricevente = message.from.id
        karmaService.takeGiveKarma(ricevente, newKarma)
    }

    private fun creditRoulette(message: Message) {
        val ricevente = message.from.id
        karmaService.incCredit(ricevente)
    }

    fun testoKarmaReport(ctx: ActionContext): String {
        val karmas = karmaService.getKarmas()
            .sortedByDescending { it.second }
            .map { "${getUserName(it.first, ctx.getChatMember).padEnd(20)}%3d".format(it.second) }
            .joinToString("\n")
        return "<b><u>Karma Report</u></b>\n\n<pre>${karmas}</pre>"
    }

    private fun getUserName(userId: Long, getChatMember: (Long) -> User?) = botUtils.getUserName(getChatMember(userId))
}
