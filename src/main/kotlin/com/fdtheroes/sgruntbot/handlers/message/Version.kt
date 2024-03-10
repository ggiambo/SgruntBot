package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.boot.info.BuildProperties
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class Version(botUtils: BotUtils, botConfig: BotConfig, buildProperties: BuildProperties) :
    MessageHandler(botUtils, botConfig), HasHalp {

    private val versionString = "${buildProperties.name}: ${buildProperties.version} (${buildProperties.time})"
    override fun handle(message: Message) {
        if (message.text == "!version") {
            botUtils.rispondi(ActionResponse.message(versionString), message)
        }
    }

    override fun halp() = "<b>!version</b> Sgrunty, parlaci di te!"

}