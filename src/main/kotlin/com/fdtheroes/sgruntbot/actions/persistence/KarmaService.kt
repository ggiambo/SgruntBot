package com.fdtheroes.sgruntbot.actions.persistence

import com.fdtheroes.sgruntbot.BotUtils
import com.fdtheroes.sgruntbot.actions.models.Karma
import jakarta.enterprise.context.ApplicationScoped
import org.telegram.telegrambots.meta.api.objects.User
import java.time.LocalDate

@ApplicationScoped
class KarmaService(
    private val botUtils: BotUtils,
    private val repo: KarmaRepository,
) {

    fun getKarma(userId: Long) = repo.getByUserId(userId)

    fun updateCredit(userId: Long, update: (Int) -> Int) {
        val karma = repo.getByUserId(userId)
        karma.karmaCredit = update(karma.karmaCredit)
        repo.persist(karma)
    }

    fun precheck(forUserId: Long) {
        val result = repo.findById(forUserId)
        if (result == null) {
            initKarmaData(forUserId)
            return
        }

        if (result.creditUpdated.isBefore(LocalDate.now())) {
            result.karmaCredit = 5
            result.creditUpdated = LocalDate.now()
            repo.persist(result)
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
        repo.persist(karma)
    }

    fun getKarmas() = repo.listAll()

    fun testoKarmaReport(getChatMember: (Long) -> User?): String {
        val karmas = repo.listAll()
            .sortedByDescending { it.karma }
            .joinToString("\n") { lineaKarmaReport(it, getChatMember) }
        return "<b><u>Karma Report</u></b>\n\n<pre>${karmas}</pre>"
    }

    private fun initKarmaData(forUserId: Long) {
        repo.persist(Karma(userId = forUserId))
    }

    private fun lineaKarmaReport(karma: Karma, getChatMember: (Long) -> User?): String {
        val userName = botUtils.getUserName(getChatMember(karma.userId!!)).padEnd(20)
        val formattedKarma = "%3d".format(karma.karma)
        return "$userName$formattedKarma"
    }

}
