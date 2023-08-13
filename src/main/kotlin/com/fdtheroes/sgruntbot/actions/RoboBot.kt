package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.Users
import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.stereotype.Service
import kotlin.random.Random.Default.nextInt

@Service
class RoboBot : Action {

    private val risposte = listOf(
        "RoboBot, tu puzzi.",
        "In questa ciat non c'è spazio per due Bot.",
        "Toh, è arrivato il mio fratellino ritardato!",
        "Tieni, un biglietto di sola andata per il rottamatore!",
        "\"BEEP-BOOP! SONO UN BOT INTELLIGENTE, YUK YUK!\"",
        "RobBot: Prototipo della stupidità artificiale"
    )

    override fun doAction(ctx: ActionContext) {
        if (ctx.message.from.id == Users.ROBOBOT.id) {
            if (nextInt(10) == 0) {
                var testo = risposte.random()
                ctx.addResponse(ActionResponse.message(testo))
            }
        }
    }
}