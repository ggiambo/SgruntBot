package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.BotConfig
import com.fdtheroes.sgruntbot.handlers.message.Slogan
import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.models.NameValuePair
import com.fdtheroes.sgruntbot.persistence.NameValuePairRepository
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.Disabled
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
@Disabled
class ScheduledRandomSlogan(
    private val botUtils: BotUtils,
    private val nameValuePairRepository: NameValuePairRepository,
    private val slogan: Slogan,
) : ScheduledRandom {

    override fun execute() {
        val lastAuthorId = nameValuePairRepository.findByIdOrNull(NameValuePair.NameValuePairName.LAST_AUTHOR)?.value
        val testo = if (lastAuthorId == null) "" else {
            val lastAuthor = botUtils.getChatMember(lastAuthorId.toLong())
            slogan.fetchSlogan(lastAuthor!!)
        }

        botUtils.messaggio(ActionResponse.message(testo))
    }

}
