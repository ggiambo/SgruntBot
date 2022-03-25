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
    private val sgruntBot: SgruntBot,
    private val botUtils: BotUtils,
    private val karmaRepository: KarmaRepository,
) {

    val dailyKarmaCredit = 5

    @Transactional
    fun precheck(forUserId: Long) {
        val result = karmaRepository.findByIdOrNull(forUserId)
        if (result == null) {
            initKarmaData(forUserId)
            return
        }

        if (result.creditUpdated.isBefore(LocalDate.now())) {
            resetCreditForToday(forUserId = forUserId)
        }
    }

    fun getKarma(forUserId: Long) = karmaRepository.findById(forUserId).get().karmaCredit

    fun getKarmaCredit(forUserId: Long) = karmaRepository.findById(forUserId).get().karmaCredit

    // donatore da' del karma a ricevente, credito di donatore diminiuisce
    @Transactional
    fun takeGiveKarma(donatore: Long, ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        takeGiveKarma(ricevente, newKarma)
        val updatedCredit = getKarmaCredit(donatore) - 1
        updateCredit(updatedCredit, donatore)
    }

    // ricevente riceve del karma
    @Transactional
    fun takeGiveKarma(ricevente: Long, newKarma: (oldKarma: Int) -> Int) {
        val updatedKarma = newKarma(karmaRepository.findById(ricevente).get().karma)
        updateKarma(updatedKarma, ricevente)
    }

    @Modifying
    fun updateKarma(updatedKarma: Int, userId: Long) {
        val karma = karmaRepository.findById(userId).get()
        karma.karma = updatedKarma
        karmaRepository.save(karma)
    }

    @Modifying
    fun updateCredit(updatedCredit: Int, userId: Long) {
        val karma = karmaRepository.findById(userId).get()
        karma.karmaCredit = updatedCredit
        karmaRepository.save(karma)
    }

    fun getKarmas(): List<Pair<Long, Int>> {
        return karmaRepository.findAll().map {
            Pair(it.userId, it.karma)
        }
    }

    @Modifying
    private fun initKarmaData(forUserId: Long) {
        val karma = Karma(
            userId = forUserId,
            karma = 0,
            karmaCredit = dailyKarmaCredit,
            creditUpdated = LocalDate.now(),
        )
        karmaRepository.save(karma)
    }

    @Modifying
    fun resetCreditForToday(
        forUserId: Long,
        updatedCredit: Int = dailyKarmaCredit,
        creditUpdated: LocalDate = LocalDate.now()
    ) {
        val karma = karmaRepository.findById(forUserId).get()
        karma.karmaCredit = updatedCredit
        karma.creditUpdated = LocalDate.now()
        karmaRepository.save(karma)
    }

    fun testoKarmaReport(): String {
        val karmas = getKarmas()
            .sortedByDescending { it.second }
            .map { "${getUserName(it.first).padEnd(20)}%3d".format(it.second) }
            .joinToString("\n")
        return "<b><u>Karma Report</u></b>\n\n<pre>${karmas}</pre>"
    }

    private fun getUserName(userId: Long) = botUtils.getUserName(sgruntBot.getChatMember(userId))

}