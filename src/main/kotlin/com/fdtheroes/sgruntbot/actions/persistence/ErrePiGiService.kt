package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.ErrePiGi
import org.springframework.stereotype.Service
import org.springframework.scheduling.annotation.Scheduled
import org.telegram.telegrambots.meta.api.objects.User
import kotlin.random.Random.Default.nextInt

@Service
class ErrePiGiService(
    private val botUtils: BotUtils,
    private val errePiGiRepository: ErrePiGiRepository,
) {

    private val attacchi = listOf(
        "un grosso tonno puzzolente",
        "il suo big bamboo",
        "fatti&logica",
        "una serie di puzzette ben piazzate",
        "una restrospettiva sul cinema muto Uzbeko fine anni '70",
        "la tecnica segreta di Ken il guerriero",
        "le sue affilatissime e laccatissime unghie",
        "le mutande di Richard Stallman",
        "un Nokia 3310",
        "la sua collezione di Mio Mini Pony",
        "la stampa completa di 'Howto install Arch Linux'",
    )

    fun init(userId: Long): ErrePiGi {
        errePiGiRepository.createErrePiGi(ErrePiGi(userId))
        return errePiGiRepository.getErrePiGiByUserId(userId)!!
    }

    @Scheduled(cron = "0 0 0 * * *")
    fun reset() {
        errePiGiRepository.deleteAll()
    }

    fun attacca(attaccante: User, difensore: User): String {
        var attaccanteErrePiGi = errePiGiRepository.getErrePiGiByUserId(attaccante.id)
        if (attaccanteErrePiGi == null) {
            attaccanteErrePiGi = init(attaccante.id)
        }
        val attaccanteName = botUtils.getUserLink(attaccante)
        if (attaccanteErrePiGi.hp <= 0) {
            return "Sei morto, non puoi attaccare.\nAspetta fino a domani per riprovare."
        }

        var difensoreErrePiGi = errePiGiRepository.getErrePiGiByUserId(difensore.id)
        if (difensoreErrePiGi == null) {
            difensoreErrePiGi = init(difensore.id)
        }
        val difensoreName = botUtils.getUserLink(difensore)
        if (difensoreErrePiGi.hp <= 0) {
            return "Vile, vuoi attaccare $difensoreName che è già morto!\nAspetta fino a domani per riprovare."
        }

        val attaccantiIds = difensoreErrePiGi.attaccantiIds
            .split(",")
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
            .toMutableList()
        if (attaccantiIds.contains(attaccante.id)) {
            return "Oggi hai già attaccato $difensoreName.\nAspetta fino a domani per riprovare."
        }

        val puntiFeritaAttaccante = nextInt(5)
        attaccanteErrePiGi.hp = Math.max(attaccanteErrePiGi.hp - puntiFeritaAttaccante, 0)
        attaccantiIds.add(attaccante.id)

        val puntiFeritaDifensore = nextInt(5)
        difensoreErrePiGi.hp = Math.max(difensoreErrePiGi.hp - puntiFeritaDifensore, 0)
        difensoreErrePiGi.attaccantiIds = attaccantiIds.joinToString(separator = ",")

        errePiGiRepository.save(attaccanteErrePiGi)
        errePiGiRepository.save(difensoreErrePiGi)

        val attacco = "<b>$attaccanteName attacca $difensoreName con ${attacchi.random()}!</b>"
        val risultatoAttaccante = "$attaccanteName ora ha ${attaccanteErrePiGi.hp} punti-vita."
        val risultatoDifensore = "$difensoreName ora ha ${difensoreErrePiGi.hp} punti-vita."

        return "$attacco\n\n$risultatoAttaccante\n$risultatoDifensore"
    }
}
