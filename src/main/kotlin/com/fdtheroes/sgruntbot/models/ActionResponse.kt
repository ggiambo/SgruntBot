package com.fdtheroes.sgruntbot.models

import org.telegram.telegrambots.meta.api.objects.InputFile

class ActionResponse(
    val type: ActionResponseType,
    val message: String? = null,
    val inputFile: InputFile? = null,
) {
    companion object {
        fun message(message: String, rispondi: Boolean = true) =
            ActionResponse(ActionResponseType.Message, message = message)

        fun photo(caption: String, inputFile: InputFile, rispondi: Boolean = true) =
            ActionResponse(ActionResponseType.Photo, message = caption, inputFile = inputFile)

        fun audio(inputFile: InputFile, rispondi: Boolean = true) =
            ActionResponse(ActionResponseType.Audio, inputFile = inputFile)
    }

}