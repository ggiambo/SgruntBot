package com.fdtheroes.sgruntbot.scheduled.fix.housekeeping

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.Scommessina
import com.fdtheroes.sgruntbot.persistence.ScommessinaService
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service

@Service
class CleanupScommessina(
    private val scommessinaService: ScommessinaService,
    private val botUtils: BotUtils,
) : Cleanup {

    override fun doCleanup() {
        deleteExpiredScommessine()
        checkWillExpireScommessine()
    }

    private fun checkWillExpireScommessine() =
        scommessinaService.getWillExpireInThreeDays()
            .groupBy { it.userId }
            .forEach { messageWillExpire(it.value) }

    private fun deleteExpiredScommessine() =
        scommessinaService.getExpired()
            .groupBy { it.userId }
            .forEach { messageDeleteExpired(it.value) }

    private fun messageWillExpire(scommessine: List<Scommessina>) =
        scommessine.forEach {
            val user = getUser(it.userId)
            val msgText = "Hei $user, la scommesssina <i>${it.content}</i> scade fra 3 giorni e verrà cancellata."
            botUtils.messaggio(ActionResponse.message(msgText))
        }

    private fun messageDeleteExpired(scommessine: List<Scommessina>) =
        scommessine.forEach {
            val user = getUser(it.userId)
            val msgText = "Hei $user, la scommesssina <i>${it.content}</i> è scaduta e verrà cancellata."
            botUtils.messaggio(ActionResponse.message(msgText))
            scommessinaService.deleteById(it.id!!)
        }

    private fun getUser(userId: Long) = botUtils.getUserLink(botUtils.getChatMember(userId))
}
