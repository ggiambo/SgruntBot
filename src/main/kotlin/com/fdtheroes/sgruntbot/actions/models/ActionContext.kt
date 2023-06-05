package com.fdtheroes.sgruntbot.actions.models

import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User

class ActionContext(
    val message: Message,
    val getChatMember: (Long) -> User?
) {

    private val _actionResponses = mutableListOf<ActionResponse>()
    val actionResponses: List<ActionResponse> = _actionResponses
    fun addResponse(actionResponse: ActionResponse) = _actionResponses.add(actionResponse)
}