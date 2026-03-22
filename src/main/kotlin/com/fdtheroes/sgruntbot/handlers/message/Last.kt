package com.fdtheroes.sgruntbot.handlers.message

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.NameValuePair
import com.fdtheroes.sgruntbot.persistence.NameValuePairRepository
import com.fdtheroes.sgruntbot.utils.BotUtils
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.objects.message.Message

@Service
class Last(
    private val nameValuePairRepository: NameValuePairRepository,
    botUtils: BotUtils,
    botConfig: BotConfig,
) : MessageHandler(botUtils, botConfig),
    HasHalp {

    private val regex = Regex("^!last$", RegexOption.IGNORE_CASE)

    override fun handle(message: Message) {
        val lastAuthorId = nameValuePairRepository.getValue(NameValuePair.NameValuePairName.LAST_AUTHOR)
        if (regex.matches(message.text) && lastAuthorId != null) {
            val lastAuthor = botUtils.getChatMember(lastAuthorId.toLong())
            botUtils.rispondi(ActionResponse.message(botUtils.getUserLink(lastAuthor)), message)
        }
    }

    override fun halp() = "<b>!last</b> uno slogan per l'ultimo autore"

}
