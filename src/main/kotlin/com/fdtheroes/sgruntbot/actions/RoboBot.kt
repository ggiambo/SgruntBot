package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.random.Random.Default.nextInt

@Service
class RoboBot : Action {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val risposte = listOf(
        "RoboBot, tu puzzi.",
        "In questa ciat non c'è spazio per due Bot.",
        "Toh, è arrivato il mio fratellino ritardato!",
        "Tieni, un biglietto di sola andata per il rottamatore!",
        "\"BEEP-BOOP! SONO UN BOT INTELLIGENTE, YUK YUK!\"",
        "RobBot: Prototipo della stupidità artificiale"
    )

    override fun doAction(ctx: ActionContext) {
        log.info("${ctx.message.from.id} == ${Users.ROBOBOT.id} ...")
        if (ctx.message.from.id == Users.ROBOBOT.id) {
            log.info("... is true!")
            if (nextInt(10) == 0) {
                log.info("-> rispondi al gengibot")
                var testo = risposte.random()
                ctx.addResponse(ActionResponse.message(testo))
            }
        }
    }
}
