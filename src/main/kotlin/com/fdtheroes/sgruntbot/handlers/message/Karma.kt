package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.persistence.KarmaService
import com.fdtheroes.sgruntbot.persistence.UsersService
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random.Default.nextInt

@Service
class Karma(
    botUtils: BotUtils,
    botConfig: BotConfig,
    private val karmaService: KarmaService,
    private val userService: UsersService,
) : MessageHandler(botUtils, botConfig), HasHalp {

    override fun handle(message: Message) {
        val ricevente = message.replyToMessage?.from?.id
        if (Regex("\\++").matches(message.text) && ricevente != null) {
            giveTakeKarma(message, ricevente, message.text.length, Int::inc)
        }
        if (Regex("-+").matches(message.text) && ricevente != null) {
            giveTakeKarma(message, ricevente, message.text.length, Int::dec)
        }
        if (message.text == "!karma") {
            botUtils.rispondi(ActionResponse.message(karmaService.testoKarmaReport()), message)
        }
    }

    override fun halp() = """
        <b>!karma</b> mostra la situazione del Karma
        <b>+</b> da un punto karma all'autore del messaggio
        <b>-</b> toglie punto karma all'autore del messaggio
        """.trimIndent()

    private fun giveTakeKarma(
        message: Message,
        ricevente: Long,
        n: Int,
        newKarma: (oldKarma: Int) -> Int
    ) {

        val utonto = userService.getUser(ricevente)
        if (utonto?.isBot == true) {
            botUtils.rispondi(
                ActionResponse.message("${utonto.firstName} è un bot senz'anima. Assegna il karma saggiamente"),
                message
            )
            return
        }

        val donatore = message.from.id
        if (donatore == ricevente) {
            botUtils.rispondi(
                ActionResponse.message("Ti è stato dato il potere di dare o togliere ad altri, ma non a te stesso"),
                message
            )
            return
        }

        karmaService.precheck(donatore)
        karmaService.precheck(ricevente)

        if (karmaService.getKarma(donatore).karmaCredit < 1) {
            botUtils.rispondi(ActionResponse.message("Hai terminato i crediti per oggi"), message)
            return
        }

        var wonKarma = 0
        var wonCredit = 0

        for (i in 1..Math.min(n, karmaService.getKarma(donatore).karmaCredit)) {
            karmaService.takeGiveKarma(donatore, ricevente, newKarma)

            if (nextInt(5) == 0) { // 20%
                karmaRoulette(message, newKarma)
                wonKarma = newKarma(wonKarma)
            }

            if (nextInt(5) == 0) { // 20%
                creditRoulette(message)
                wonCredit++
            }
        }

        val riceventeLink = botUtils.getUserLink(message.replyToMessage.from)
        val donatoreLink = botUtils.getUserLink(message.from)
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

        botUtils.rispondi(ActionResponse.message(karmaMessage), message)
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
