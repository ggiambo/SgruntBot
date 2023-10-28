package com.fdtheroes.sgruntbot.scheduled.fix

import com.fdtheroes.sgruntbot.Bot
import com.fdtheroes.sgruntbot.actions.persistence.ErrePiGiRepository
import com.fdtheroes.sgruntbot.actions.persistence.KarmaRepository
import com.fdtheroes.sgruntbot.actions.persistence.StatsRepository
import com.fdtheroes.sgruntbot.actions.persistence.UtontiRepository
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class Cleanup(
    private val bot: Bot,
    private val errePiGiRepository: ErrePiGiRepository,
    private val karmaRepository: KarmaRepository,
    private val statsRepository: StatsRepository,
    private val utontiRepository: UtontiRepository,
) : Scheduled {
    override fun firstRun() = LocalDateTime.now()

    override fun nextRun() = LocalDateTime.now().plusHours(6)

    override fun execute() {
        val fantasmi = utontiRepository.findAll()
            .map { it.userId!! }
            .filter { bot.getChatMember(it) == null }
        errePiGiRepository.deleteAllByUserIdIn(fantasmi)
        karmaRepository.deleteAllByUserIdIn(fantasmi)
        statsRepository.deleteAllByUserIdIn(fantasmi)
        utontiRepository.deleteAllByUserIdIn(fantasmi)
    }
}