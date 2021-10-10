package com.fdtheroes.sgruntbot.utils

import org.telegram.telegrambots.meta.api.objects.Message

class UserUtils {

    fun getUserLink(message: Message?): String {
        if (message == null) {
            return ""
        }
        val id = message.from.id
        var name: String
        if (message.from?.userName != null) {
            name = message.from.userName
        } else {
            name = message.from.firstName
        }
        return "[${name}](tg://user?id=${id})"
    }
}