package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.data.jdbc.repository.query.Modifying
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class KarmaService(
    private val botUtils: BotUtils,
    private val repo: KarmaRepository,
) {

    fun getKarma(userId: Long) = repo.getKarma(userId)

    fun getKarmaCredit(userId: Long) = repo.getKarmaCredit(userId)

    @Transactional
    fun precheck(forUserId: Long) {
        val result = repo.findByIdOrNull(forUserId)
        if (result == null) {
            initKarmaData(forUserId)
            return
        }

        if (result.creditUpdated.isBefore(LocalDate.now())) {
            repo.resetCreditForToday(forUserId)
        }
    }

    // donatore da' del karma a ricevente, credito di donatore diminiuisce
    @Transactional
    fun takeGiveKarma(donatore: Long, ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        takeGiveKarma(ricevente, newKarma)
        val updatedCredit = repo.getKarmaCredit(donatore) - 1
        repo.updateCredit(updatedCredit, donatore)
    }

    // ricevente riceve del karma
    @Transactional
    fun takeGiveKarma(ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        val updatedKarma = newKarma(repo.getKarma(ricevente))
        repo.updateKarma(updatedKarma, ricevente)
    }

    @Transactional
    fun incCredit(ricevente: Long) {
        val credit = repo.getKarmaCredit(ricevente)
        repo.updateCredit(credit + 1, ricevente)
    }

    fun getKarmas(): List<Pair<Long, Int>> {
        return repo.findAll().map {
            Pair(it.userId, it.karma)
        }
    }

    fun testoKarmaReport(sgruntBot: SgruntBot): String {
        val karmas = getKarmas()
            .sortedByDescending { it.second }
            .map { "${getUserName(it.first, sgruntBot).padEnd(20)}%3d".format(it.second) }
            .joinToString("\n")
        return "<b><u>Karma Report</u></b>\n\n<pre>${karmas}</pre>"
    }

    @Modifying
    private fun initKarmaData(forUserId: Long) {
        repo.save(Karma(userId = forUserId))
    }

    private fun getUserName(userId: Long, sgruntBot: SgruntBot): String {
        return botUtils.getUserName(sgruntBot.getChatMember(userId))
    }

}