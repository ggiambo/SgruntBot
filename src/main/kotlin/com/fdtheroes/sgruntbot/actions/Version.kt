package com.fdtheroes.sgruntbot.actions

import com.fdtheroes.sgruntbot.SgruntBot
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Version(sgruntBot: SgruntBot, buildProperties: BuildProperties) : Action(sgruntBot), HasHalp {

    private val versionString = "${buildProperties.name}: ${buildProperties.version} (${buildProperties.time})"
    override fun doAction(message: Message) {
        if (message.text == "!version") {
            sgruntBot.rispondiAsText(message, versionString)
        }
    }

    override fun halp() = "<b>!version</b> Sgrunty, parlaci di te!"

}