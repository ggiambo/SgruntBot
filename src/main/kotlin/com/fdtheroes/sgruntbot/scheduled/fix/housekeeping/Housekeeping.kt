package com.fdtheroes.sgruntbot.scheduled.fix.housekeeping

import com.fdtheroes.sgruntbot.scheduled.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class
Housekeeping(private val jobs: List<Cleanup>) : Scheduled {

    override fun firstRun(): LocalDateTime = LocalDateTime.now()

    override fun nextRun(): LocalDateTime = LocalDateTime.now().plusHours(6)

    override fun execute() {
        jobs.forEach { it.doCleanup() }
    }

}