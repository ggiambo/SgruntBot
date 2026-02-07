package com.fdtheroes.sgruntbot.scheduled.fix.housekeeping

import com.fdtheroes.sgruntbot.persistence.ErrePiGiRepository
import com.fdtheroes.sgruntbot.persistence.KarmaRepository
import com.fdtheroes.sgruntbot.persistence.StatsRepository
import com.fdtheroes.sgruntbot.persistence.TodosRepository
import com.fdtheroes.sgruntbot.persistence.UtontiRepository
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CleanupUtenti(
    private val botUtils: BotUtils,
    private val errePiGiRepository: ErrePiGiRepository,
    private val karmaRepository: KarmaRepository,
    private val statsRepository: StatsRepository,
    private val utontiRepository: UtontiRepository,
    private val todosRepository: TodosRepository,
) : Cleanup {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun doCleanup() {
        val viventi = utontiRepository.findAll()
            .map { it.userId!! }
            .filter { botUtils.getChatMember(it) != null }
        if (viventi.isNotEmpty()) {
            log.info("I seguenti utonti sono ancora vivi: ${viventi.joinToString()}")
            logDelete(errePiGiRepository.deleteAllByUserIdNotIn(viventi))
            logDelete(karmaRepository.deleteAllByUserIdNotIn(viventi))
            logDelete(statsRepository.deleteAllByUserIdNotIn(viventi))
            logDelete(utontiRepository.deleteAllByUserIdNotIn(viventi))
            logDelete(todosRepository.deleteAllByUserIdNotIn(viventi))
        }
    }

    private fun logDelete(entities: List<*>) {
        if (entities.isNotEmpty()) {
            log.info("Deleted: $entities")
        }
    }
}