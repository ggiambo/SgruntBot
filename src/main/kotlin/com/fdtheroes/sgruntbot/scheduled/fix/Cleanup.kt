package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.persistence.*
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

//@Service
class Cleanup(
    private val botUtils: BotUtils,
    private val errePiGiRepository: ErrePiGiRepository,
    private val karmaRepository: KarmaRepository,
    private val statsRepository: StatsRepository,
    private val utontiRepository: UtontiRepository,
    private val todosRepository: TodosRepository,
) : Scheduled {

    private val log = LoggerFactory.getLogger(this.javaClass)
    override fun firstRun() = LocalDateTime.now()

    override fun nextRun() = LocalDateTime.now().plusHours(6)

    override fun execute() {
        val fantasmi = utontiRepository.findAll()
            .map { it.userId!! }
            .filter { botUtils.getChatMember(it) == null }
        if (fantasmi.isNotEmpty()) {
            log.info("Cancello i seguenti fantasmi: ${fantasmi.joinToString()}")
            errePiGiRepository.deleteAllByUserIdIn(fantasmi)
            karmaRepository.deleteAllByUserIdIn(fantasmi)
            statsRepository.deleteAllByUserIdIn(fantasmi)
            utontiRepository.deleteAllByUserIdIn(fantasmi)
            todosRepository.deleteAllByUserIdIn(fantasmi)
        }
    }
}
