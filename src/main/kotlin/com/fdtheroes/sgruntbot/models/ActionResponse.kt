package com.fdtheroes.sgruntbot.models

import org.telegram.telegrambots.meta.api.objects.InputFile

class ActionResponse(
    val type: ActionResponseType,
    val message: String,
    val inputFile: InputFile? = null,
) {
    companion object {
        fun message(message: String) =
            ActionResponse(ActionResponseType.Message, message)

        fun photo(caption: String, inputFile: InputFile) =
            ActionResponse(ActionResponseType.Photo, caption, inputFile)

        fun audio(filename: String, inputFile: InputFile) =
            ActionResponse(ActionResponseType.Audio, filename, inputFile)
    }

}