package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import kotlin.random.Random.Default.nextInt

@Service
class Karma(
    private val botUtils: BotUtils,
    private val karmaService: KarmaService,
) : Action, HasHalp {

    override fun doAction(ctx: ActionContext) {
        val ricevente = ctx.message.replyToMessage?.from?.id
        if (ctx.message.text == "+" && ricevente != null) {
            giveTakeKarma(ctx, ricevente, Int::inc)
        }
        if (ctx.message.text == "-" && ricevente != null) {
            giveTakeKarma(ctx, ricevente, Int::dec)
        }
        if (ctx.message.text == "!karma") {
            ctx.addResponse(ActionResponse.message(testoKarmaReport(ctx)))
        }
    }

    override fun halp() = """
        <b>!karma</b> mostra la situazione del Karma
        <b>+</b> da un punto karma all'autore del messaggio
        <b>-</b> togle punto karma all'autore del messaggio
        """.trimIndent()

    private fun giveTakeKarma(
        ctx: ActionContext,
        ricevente: Long,
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

        karmaService.takeGiveKarma(donatore, ricevente, newKarma)

        val riceventeLink = botUtils.getUserLink(ctx.message.replyToMessage.from)
        val donatoreLink = botUtils.getUserLink(ctx.message.from)
        val karma = karmaService.getKarma(ricevente)
        val crediti = karmaService.getKarmaCredit(donatore)
        var karmaMessage = "Karma totale di $riceventeLink: $karma\nCrediti di $donatoreLink: $crediti"

        if (nextInt(5) == 0) { // 20%
            val karmaRoulette = karmaRoulette(ctx.message, newKarma)
            karmaMessage = karmaMessage.plus("\n\n$karmaRoulette")
        }

        if (nextInt(5) == 0) { // 20%
            val creditRoulette = creditRoulette(ctx.message)
            karmaMessage = karmaMessage.plus("\n\n$creditRoulette")
        }

        ctx.addResponse(ActionResponse.message(karmaMessage))
    }

    private fun karmaRoulette(message: Message, newKarma: (oldKarma: Int) -> Int): String {
        val ricevente = message.from.id
        karmaService.takeGiveKarma(ricevente, newKarma)
        val karma = karmaService.getKarma(ricevente)
        return "<b>Karmaroulette</b> ! Il tuo Karma è ora di $karma"
    }

    private fun creditRoulette(message: Message): String {
        val ricevente = message.from.id
        karmaService.incCredit(ricevente)
        val credit = karmaService.getKarmaCredit(ricevente)
        return "<b>Creditroulette</b> ! Hai vinto un credito, ora sei a quota $credit"
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
