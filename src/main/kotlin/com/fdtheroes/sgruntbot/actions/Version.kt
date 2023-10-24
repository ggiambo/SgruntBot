package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.actions.models.ActionContext
import com.fdtheroes.sgruntbot.actions.models.ActionResponse
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.ConfigProvider

@ApplicationScoped
class Version : Action, HasHalp {

    private lateinit var versionString: String

    init {
        val config = ConfigProvider.getConfig()
        val name = config.getValue("quarkus.application.name", String::class.java)
        val version = config.getValue("quarkus.application.version", String::class.java)
        val time = config.getValue("quarkus.info.build", Map::class.java)
        versionString = "$name: $version ($time)"
    }

    override fun doAction(ctx: ActionContext) {
        if (ctx.message.text == "!version") {
            ctx.addResponse(ActionResponse.message(versionString))
        }
    }

    override fun halp() = "<b>!version</b> Sgrunty, parlaci di te!"

}
