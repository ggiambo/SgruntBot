package com.fdtheroes.sgruntbot.persistence

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.models.ErrePiGi
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.User
import kotlin.math.max
import kotlin.random.Random.Default.nextInt

@Service
class ErrePiGiService(private val botUtils: BotUtils, private val errePiGiRepository: ErrePiGiRepository) {

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
        "pene&botte",
        "le tettone di Brì che ... smuà",
        "le migliori barzellette di Gino Bramieri",
        "il fondotinta di GRANDSSIMO SILVIO",
        "un container docker",
        "una tavoletta di parquet",
        "un tuo ricordo imbarazzante di quando eri tredicenne",
        "le ditone pasticcione di Giambo"
    )

    fun init(userId: Long): ErrePiGi {
        errePiGiRepository.save(ErrePiGi(userId = userId))
        return errePiGiRepository.findById(userId).get()
    }

    fun reset() {
        errePiGiRepository.findAll().forEach {
            it.hp = 10
            it.attaccantiIds = ""
            errePiGiRepository.save(it)
        }
    }

    fun testoErrePiGiReport(): String? {
        val errePiGis = errePiGiRepository.findAll()
            .filterNot {
                it.attaccantiIds.isEmpty()
            }
        if (errePiGis.isEmpty()) {
            return null
        }
        return errePiGis.joinToString(separator = "\n") { getTestoReport(it) }
    }

    fun attacca(attaccante: User, difensore: User): String {
        var attaccanteErrePiGi = errePiGiRepository.findById(attaccante.id).orElse(null)
        if (attaccanteErrePiGi == null) {
            attaccanteErrePiGi = init(attaccante.id)
        }
        if (attaccanteErrePiGi.hp <= 0) {
            return "Sei morto, non puoi attaccare.\nAspetta fino a domani per riprovare."
        }

        var difensoreErrePiGi = errePiGiRepository.findById(difensore.id).orElse(null)
        if (difensoreErrePiGi == null) {
            difensoreErrePiGi = init(difensore.id)
        }
        val difensoreName = botUtils.getUserLink(difensore)
        if (difensoreErrePiGi.hp <= 0) {
            return "Vile, vuoi attaccare $difensoreName che è già morto!\nAspetta fino a domani per riprovare."
        }

        if (difensore.id == Users.SHDX_T.id) {
            return "Seu gode della protezione di Sgrunty. Seu non si può attaccare. Vergognati per averci solamente provato."
        }

        val attaccantiIds = getAttaccantiIds(difensoreErrePiGi)
        if (attaccantiIds.contains(attaccante.id)) {
            return "Oggi hai già attaccato $difensoreName.\nAspetta fino a domani per riprovare."
        }

        return eseguiAttacco(attaccanteErrePiGi, difensoreErrePiGi)
    }

    fun sgruntyAttacca(): String? {
        val sgruntyId = Users.BLAH_BANF_BOT.id
        var sgruntyErrePiGi = errePiGiRepository.findById(sgruntyId).orElse(null)
        if (sgruntyErrePiGi == null) {
            sgruntyErrePiGi = init(Users.BLAH_BANF_BOT.id)
        }
        if (sgruntyErrePiGi.hp <= 0) {
            return null
        }

        val difensoreErrePiGi = errePiGiRepository.findAll()
            .filter { it.userId != sgruntyId }
            .filter { it.userId != Users.SHDX_T.id }
            .filter { it.hp > 0 }
            .filterNot { getAttaccantiIds(it).contains(sgruntyId) }
            .randomOrNull()

        if (difensoreErrePiGi == null) {
            return null
        }

        return eseguiAttacco(sgruntyErrePiGi, difensoreErrePiGi)
    }

    private fun eseguiAttacco(
        attaccanteErrePiGi: ErrePiGi,
        difensoreErrePiGi: ErrePiGi,
    ): String {
        val attaccantiIds = getAttaccantiIds(difensoreErrePiGi).toMutableList()

        val puntiFeritaAttaccante = nextInt(5)
        attaccanteErrePiGi.hp = max(attaccanteErrePiGi.hp - puntiFeritaAttaccante, 0)
        attaccantiIds.add(attaccanteErrePiGi.userId!!)

        val puntiFeritaDifensore = nextInt(5)
        difensoreErrePiGi.hp = max(difensoreErrePiGi.hp - puntiFeritaDifensore, 0)
        difensoreErrePiGi.attaccantiIds = attaccantiIds.joinToString(separator = ",")

        errePiGiRepository.save(attaccanteErrePiGi)
        errePiGiRepository.save(difensoreErrePiGi)

        val attaccanteName = botUtils.getUserName(botUtils.getChatMember(attaccanteErrePiGi.userId!!))
        val difensoreName = botUtils.getUserLink(botUtils.getChatMember(difensoreErrePiGi.userId!!))

        val attacco = "<b>$attaccanteName attacca $difensoreName con ${attacchi.random()}!</b>"
        val risultatoAttaccante = "$attaccanteName ${getStato(attaccanteErrePiGi)}."
        val risultatoDifensore = "$difensoreName ${getStato(difensoreErrePiGi)}."

        return "$attacco\n\n$risultatoAttaccante\n$risultatoDifensore"
    }

    private fun getAttaccantiIds(errePiGi: ErrePiGi): List<Long> {
        return errePiGi.attaccantiIds
            .split(",")
            .filter { it.isNotEmpty() }
            .map { it.toLong() }
    }

    private fun getTestoReport(errePiGi: ErrePiGi): String {
        val utente = botUtils.getChatMember(errePiGi.userId!!)
        var nomiAttaccanti = getAttaccantiIds(errePiGi)
            .map { botUtils.getChatMember(it) }
            .joinToString { botUtils.getUserName(it) }
        if (nomiAttaccanti.isEmpty()) {
            nomiAttaccanti = "nessuno"
        }
        return "${botUtils.getUserName(utente)} ${getStato(errePiGi)}. È stato attaccato da $nomiAttaccanti."
    }

    private fun getStato(errePiGi: ErrePiGi): String {
        if (errePiGi.hp <= 0) {
            return "è moruto"
        }
        return "ha ${errePiGi.hp} punti-vita"
    }
}
