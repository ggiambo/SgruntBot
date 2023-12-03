package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import com.fdtheroes.sgruntbot.actions.persistence.KarmaService
import com.fdtheroes.sgruntbot.actions.persistence.UsersService
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random.Default.nextInt

@Service
class Karma(
    private val botUtils: BotUtils,
    private val karmaService: KarmaService,
    private val userService: UsersService,
) : Action, HasHalp {

    override fun doAction(ctx: ActionContext) {
        val ricevente = ctx.message.replyToMessage?.from?.id
        if (Regex("\\++").matches(ctx.message.text) && ricevente != null) {
            giveTakeKarma(ctx, ricevente, ctx.message.text.length, Int::inc)
        }
        if (Regex("-+").matches(ctx.message.text) && ricevente != null) {
            giveTakeKarma(ctx, ricevente, ctx.message.text.length, Int::dec)
        }
        if (ctx.message.text == "!karma") {
            ctx.addResponse(ActionResponse.message(karmaService.testoKarmaReport(ctx.getChatMember)))
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

        val utonto = userService.getUser(ricevente)
        if (utonto?.isBot == true) {
            ctx.addResponse(ActionResponse.message("${utonto.firstName} è un bot senz'anima. Assegna il karma saggiamente"))
            return
        }

        val donatore = ctx.message.from.id
        if (donatore == ricevente) {
            ctx.addResponse(ActionResponse.message("Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso"))
            return
        }

        karmaService.precheck(donatore)
        karmaService.precheck(ricevente)

        if (karmaService.getKarma(donatore).karmaCredit < 1) {
            ctx.addResponse(ActionResponse.message("Hai terminato i crediti per oggi"))
            return
        }

        var wonKarma = 0
        var wonCredit = 0

        for (i in 1..n) {
            if (karmaService.getKarma(donatore).karmaCredit < 1) {
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
        val newKarmaRicevente = max(0, karmaService.getKarma(ricevente).karma)
        val crediti = karmaService.getKarma(donatore).karmaCredit
        var karmaMessage = "Karma totale di $riceventeLink: $newKarmaRicevente\nCrediti di $donatoreLink: $crediti"

        if (wonKarma != 0) {
            val newKarmaDonatore = karmaService.getKarma(donatore).karma
            val vintoPerso = if (wonKarma > 0) "vinto" else "perso"
            karmaMessage =
                karmaMessage.plus("\n\n<b>Karmaroulette</b> ! Hai $vintoPerso ${abs(wonKarma)} karma, e ora sei a quota $newKarmaDonatore")
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
        karmaService.updateCredit(ricevente, Int::inc)
    }

}
