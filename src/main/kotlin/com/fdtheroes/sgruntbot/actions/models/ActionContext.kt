package com.fdtheroes.sgruntbot.actions.models

import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User

class ActionContext(
    val message: Message,
    val getChatMemer: (Long) -> User?
) {

    private val actionResponses = mutableListOf<ActionResponse>()
    fun getActionResponses(): List<ActionResponse> = actionResponses
    fun addResponse(actionResponse: ActionResponse) = actionResponses.add(actionResponse)
}