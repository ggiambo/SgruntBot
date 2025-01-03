package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.persistence.*
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class Cleanup(
    private val botUtils: BotUtils,
    private val errePiGiRepository: ErrePiGiRepository,
    private val karmaRepository: KarmaRepository,
    private val statsRepository: StatsRepository,
    private val utontiRepository: UtontiRepository,
    private val todosRepository: TodosRepository,
) : Scheduled {

    private val log = LoggerFactory.getLogger(this.javaClass)
    override fun firstRun(): LocalDateTime = LocalDateTime.now()

    override fun nextRun(): LocalDateTime = LocalDateTime.now().plusHours(6)

    override fun execute() {
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
