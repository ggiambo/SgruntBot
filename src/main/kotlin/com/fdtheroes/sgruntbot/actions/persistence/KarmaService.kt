package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.Karma
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class KarmaService(
    private val botUtils: BotUtils,
    private val repo: KarmaRepository,
) {

    fun getKarma(userId: Long) = repo.getByUserId(userId)

    fun updateCredit(userId: Long, update: (Int) -> Int) {
        val karma = repo.getByUserId(userId)
        karma.karmaCredit = update(karma.karmaCredit)
        repo.save(karma)
    }

    fun precheck(forUserId: Long) {
        val result = repo.findByIdOrNull(forUserId)
        if (result == null) {
            initKarmaData(forUserId)
            return
        }

        if (result.creditUpdated.isBefore(LocalDate.now())) {
            result.karmaCredit = 0
            result.creditUpdated = LocalDate.now()
            repo.save(result)
        }
    }

    // donatore da' del karma a ricevente, credito di donatore diminiuisce
    fun takeGiveKarma(donatore: Long, ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        takeGiveKarma(ricevente, newKarma)
        updateCredit(donatore, Int::dec)
    }

    // ricevente riceve del karma
    fun takeGiveKarma(ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        val karma = repo.getByUserId(ricevente)
        karma.karma = newKarma(karma.karma)
        repo.save(karma)
    }

    fun getKarmas(): List<Pair<Long, Int>> {
        return repo.findAll().map {
            Pair(it.userId!!, it.karma)
        }
    }

    fun testoKarmaReport(sgruntBot: Bot): String {
        val karmas = getKarmas()
            .sortedByDescending { it.second }
            .joinToString("\n") { lineaKarmaReport(it.first, it.second, sgruntBot) }
        return "<b><u>Karma Report</u></b>\n\n<pre>${karmas}</pre>"
    }

    private fun initKarmaData(forUserId: Long) {
        repo.save(Karma(userId = forUserId))
    }

    private fun lineaKarmaReport(userId: Long, karma: Int, sgruntBot: Bot): String {
        val userName = botUtils.getUserName(sgruntBot.getChatMember(userId))
        val formattedKarma = "%3d".format(karma)
        return "{$userName.padEnd(20)}$formattedKarma"
    }

}