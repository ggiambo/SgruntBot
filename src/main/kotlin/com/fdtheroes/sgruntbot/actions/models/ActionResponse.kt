package com.fdtheroes.sgruntbot.actions.models

import org.telegram.telegrambots.meta.api.objects.InputFile

class ActionResponse(
    val type: ActionResponseType,
    val message: String? = null,
    val inputFile: InputFile? = null,
) {
    companion object {
        fun message(message: String) = ActionResponse(ActionResponseType.Message, message = message)
        fun photo(inputFile: InputFile) = ActionResponse(ActionResponseType.Photo, inputFile = inputFile)
        fun audio(inputFile: InputFile) = ActionResponse(ActionResponseType.Audio, inputFile = inputFile)
    }

}