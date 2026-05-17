package com.fdtheroes.sgruntbot.models

import org.telegram.telegrambots.meta.api.objects.InputFile

class ActionResponse(
    val type: ActionResponseType,
    val message: String,
    val inputFile: InputFile? = null,
    val thumbnail: InputFile? = null,
) {
    companion object {
        fun message(message: String) =
            ActionResponse(ActionResponseType.Message, message)

        fun photo(caption: String, inputFile: InputFile) =
            ActionResponse(ActionResponseType.Photo, caption, inputFile)

        fun audio(message: String, inputFile: InputFile, thumbnail: InputFile? = null) =
            ActionResponse(ActionResponseType.Audio, message, inputFile, thumbnail)

        fun document(message: String, inputFile: InputFile) =
            ActionResponse(ActionResponseType.Document, message, inputFile)
    }

}