package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Service

@Service
class Version(buildProperties: BuildProperties) : Action, HasHalp {

    private val versionString = "${buildProperties.name}: ${buildProperties.version} (${buildProperties.time})"
    override fun doAction(ctx: ActionContext, doNextAction: () -> Unit) {
        if (ctx.message.text == "!version") {
            ctx.addResponse(ActionResponse.message(versionString))
        }
    }

    override fun halp() = "<b>!version</b> Sgrunty, parlaci di te!"

}